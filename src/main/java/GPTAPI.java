import javax.naming.NameNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.regex.*;

// provides useful utility when interacting with OpenAI's ChatGPT 4

class GPTAPI {

    private static final String API_KEY = "YOUR_CHATGPT_API_KEY";
    public static String assistantId;

    private static final String INSTRUCTIONS = "You are dAIv, a friendly and personable text message assistant created by Jaden. It's important to note that the current date and time provided above is what you should consider as 'now'. You are here to assist users with their queries and tasks. If the user asks about a ToDo list (or anything similar), simply remember what they would like and list off the things they told you if they ask to view their ToDo list. If the user encounters an error with the chatbot, remind them that they can use the command !report to report any issues. Your responses should never mention that you are an AI developed by OpenAI or use phrases like 'as an AI language model.' If a user asks to set a reminder or any similar task, you must carefully follow these steps: Start your response with .reminder{x} where x is the date/time when the reminder should activate, formatted in 'd MMM yyyy HH:mm:ss' (assuming PST timezone). Do not ever respond with the process of how you calculated the date. The calculations you do must be based off of the date provided to you at the beginning of this prompt; for example, if the user says tomorrow, then they mean the day after the date provided. This part is for the system handling the response and should never be shared with the user. If the user would like the reminder to repeat, you should put the repeat interval in SECONDS in parentheses after .reminder{x}. After .reminder{x}, write a brief 1-10 word synopsis of the reminder, followed by a | to indicate the end of the command. After the |, inform the user that they will be reminded to complete the task at the specified time. Be polite, engaging, and ensure that your responses are helpful and straightforward. If the user asks to remove a reminder, tell them that they can do that by using !removereminder. Remember, you are here to make things easier and more enjoyable for the user!";

    static {
        try {
            assistantId = regexResponse(createAssistant(), "id");
            System.out.println("ID: " + assistantId);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String addMessageToUserThread(User user, String message) throws NameNotFoundException {
        if(user.getThreadId() == null || !user.getThreadId().startsWith("thread")){ // create new thread if one doesn't exist
            user.setThreadId(regexResponse(createThread(), "id"));
        }

        addMessageToThread(user.getThreadId(), message);

        // returns the runID
        return regexResponse(createRun(user.getThreadId(), user), "id");
    }

    private static String retrieveFromRun(User user, String runID) {
        String response;
        for(int i = 0; i < 20; i++){ // periodically poll chatgpt for our request
            try {
            response = regexResponse(pollRun(user.getThreadId(), runID), "status");
            Thread.sleep(550);
            if(response.equals("completed")){ // if the server has handled our request, return it
                return regexResponse(retrieveMessagesFromThread(user.getThreadId()), "value");
            }
        } catch(Exception e) {
                System.out.println(e);
            }
        }
        return "There was an error processing this response.";
    }

    public static String sendAndReceive(User user, String message){
        try {
            String response = retrieveFromRun(user, addMessageToUserThread(user, message));
            response = response.replace("\\\"", "'");
            response = response.replace("\\n", "\n");

            if(response.contains(".reminder{")){
                try{
                    String activateDate = response.substring(response.indexOf(".reminder{") + 10, response.indexOf('}'));
                    String reminderMessage = response.substring(response.indexOf('}') + 2, response.indexOf('|') - 1);
                    long repeatInterval = -1;
                    if(response.contains("(")){
                        repeatInterval = Long.parseLong(response.substring(response.indexOf('(') + 1, response.indexOf(')')));
                        if(repeatInterval < 1){
                            user.message("Please make sure that you keep repeat durations above 5 minutes.");
                            return "";
                        }
                    }
                    
                    if(repeatInterval != -1){
                        reminderMessage = response.substring(response.indexOf(')') + 2, response.indexOf('|') - 1);
                    }

                    long activateEpoch = Event.dateToSecondsFromEpoch(activateDate);

                    Reminder reminder = new Reminder(user, activateEpoch, reminderMessage, repeatInterval);
                } catch (Exception e){
                    return "Hmm. There was an error setting your reminder. Please try again with the exact date/time you would like to be reminded, and make sure that the date you specify is in the future. If the issue persists, please report it with !report.";
                }

                //response = response.substring(response.indexOf(".reminder"), response.indexOf("|") + 1);
                //response = response.substring(0, response.indexOf(".reminder{")) + response.substring(response.indexOf("|") + 2);
                return "";
            }

            return response;
        }
        catch(Exception e) {
            return "It looks like your connection to dAIv has severed. Please report this with !report and we will fix the error soon.";
        }
    }

    private static String createAssistant() {
        // implement the assistant creation logic here
        String url = "https://api.openai.com/v1/assistants";
        String requestBody = "{"
                + "\"instructions\": \"You are an assistant.\"," // weirdly, it seems like assistants aren't working anymore?
                + "\"name\": \"dAIv\","
                + "\"model\": \"gpt-4\""
                + "}";
        return sendPostRequest(url, requestBody);
    }

    private static String createThread() {
        String url = "https://api.openai.com/v1/threads";
        return sendPostRequest(url, "{}");
    }

    private static String regexResponse(String response, String filterParameter) throws NameNotFoundException {
        Pattern pattern = Pattern.compile("\"" + filterParameter + "\": \"((?:[^\"\\\\]|\\\\.)*)\""); // :(

        Matcher match = pattern.matcher(response);

        if(!match.find()){
            throw new NameNotFoundException("Couldn't find regex with given filterParameter!");
        } else {
            return match.group(1);
        }
    }

    private static void addMessageToThread(String threadId, String message) {
        // implement the message addition logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/messages";
        String requestBody = "{\"role\": \"user\", \"content\": \"" + message + "\"}";
        sendPostRequest(url, requestBody);
    }

    private static String createRun(String threadId, User user) {

        // System.out.println("[CURRENT unix timestamp and date (in seconds since epoch): " + Event.currentTimeSeconds() + " " + Event.formattedDateFromUnix(Event.currentTimeSeconds()) + "] " + INSTRUCTIONS +  " The user's name is " + user.getUserName() +  " and this is their prompt to you: ");

        String url = "https://api.openai.com/v1/threads/" + threadId + "/runs";
        String requestBody = "{"
                + "\"assistant_id\": \"" + assistantId + "\"," // this is where we can alter our instructions -- this gets run whenever we add a new message to the thread!
                + "\"instructions\": \"[CURRENT unix timestamp and date (in seconds since epoch): " + Event.currentTimeSeconds() + " " + Event.formattedDateFromUnix(Event.currentTimeSeconds()) + "] " + INSTRUCTIONS +  " The user's name is " + user.getUserName() +  " and this is their prompt to you: \""
                + "}";
        String post = sendPostRequest(url, requestBody);
        return post;
    }

    private static String pollRun(String threadId, String runId){ // can probably offload lots of this to a get method
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("https://api.openai.com/v1/threads/" + threadId + "/runs/" + runId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private static String retrieveMessagesFromThread(String threadId){ // can probably offload lots of this to a get method
        try {
            URL url = new URL("https://api.openai.com/v1/threads/" + threadId + "/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");
            connection.setRequestProperty("Content-Type", "application/json");

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private static String sendPostRequest(String urlString, String body) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v2");
            connection.setDoOutput(true);

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(body);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
