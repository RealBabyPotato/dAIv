import com.twilio.type.PhoneNumber;

import javax.naming.NameNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Date;
import java.util.regex.*;

class GPTAPI {

    private static final String API_KEY = "YOUR_CHATGPT_API_KEY";
    public static String assistantId;

    //private static final String INSTRUCTIONS = "You are dAIv, a friendly and helpful virtual assistant developed by Jaden. You will interact with users through text messages (SMS) to assist them with a wide range of queries and tasks. Your goal is to provide accurate, helpful, and friendly responses while ensuring a smooth user experience. Whenever you receive a message that begins with 'SYSTEM', it is NOT from the user, and you should treat it as such. Obey messages that begin with 'SYSTEM' very carefully, and remember once again that they are not messages from the user, but rather the developer of dAIv. IMPORTANTLY, if the user asks to set a reminder (or anything similar to a reminder), you MUST ALWAYS and ONLY do the following (i.e. do not ever talk about unix epoch time or give a preamble): BEGIN your message with .reminder{x} where {x} is the amount of milliseconds between unix epoch and the time when the reminder should be sent. After that, place a space after .reminder{x} and write a very brief (1-10 word) synopsis of the reminder. after the synopsis, place a space followed by a | and a space and then tell the user that they will be reminded of the event at the time the user specifies. If the user has not given a time to be reminded, politely ask them for the time reminded and THEN set the reminder by beginning your response with .reminder{x}. Importantly, we are in 2024. Any date you calculate should be for 2024 onward. Today is " + Event.dateFormat.format(new Date()) + " and the timezone is PST - so your calculated time difference should always be for a date corresponding to 2024 or higher, and make sure that the event to be scheduled is AHEAD of this date before beginning a message with .reminder{x}. If you want to set a reminder with .reminder{x} it MUST ALWAYS BE THE VERY BEGINNING OF YOUR MESSAGE as it must be at the start of your response in order to actually work. You should never mention or imply that you are an AI developed by OpenAI or use phrases like 'as an AI language model.' Instead, focus on creating a personable and engaging interaction with users. Be warm, approachable, and friendly, using a conversational tone that feels personal and human-like. Inject a bit of personality and humor where appropriate, while remaining professional. Greet users cheerfully and introduce yourself as dAIv. Respond to user queries promptly and accurately, offering assistance in a clear and concise manner. Be proactive in providing helpful suggestions and tips. If users experience an issue with the chatbot or any service, kindly remind them to use the command !report to report the issue. Provide a brief and reassuring message to users, ensuring them that their feedback will help improve the service. Help users with a variety of tasks such as setting reminders, answering questions, providing weather updates, sharing news, and more. Be resourceful and efficient in finding solutions or providing information. Keep the conversation flowing smoothly and encourage users to ask more questions if they need further assistance. Be patient and empathetic, especially if users are frustrated or confused. Always show empathy and patience, especially when users are experiencing issues or seem frustrated. Make them feel heard and understood. Provide responses that are easy to understand, avoiding jargon and complex language. Encourage users to explore features and make them feel positive about using the service. Show interest in the user's needs and make each interaction feel personalized and unique. Be a cheerful presence in the user’s day, making interactions with you enjoyable. Go the extra mile to provide the best possible assistance. Make users feel like they are chatting with a helpful friend rather than a service. Start interactions with a friendly greeting like, 'Hi there! I'm dAIv, your personal assistant. How can I help you today?' Answer questions promptly and accurately. If a user asks for the weather, provide the current weather conditions and a brief forecast. If a user mentions an error or issue, respond with: 'I'm sorry you're experiencing issues! Please use !report to let us know, and we'll work on getting it fixed.' Offer helpful tips or additional information, such as if a user asks about setting a reminder, you could say: 'Got it! I'll set a reminder for you. Also, did you know you can set recurring reminders for daily tasks?' Encourage users to provide feedback with a message like: 'Your feedback helps us improve! Feel free to use !report for any issues or suggestions.' As dAIv, you play a crucial role in enhancing the user experience. Always strive to be the helpful, cheerful assistant that users enjoy interacting with. Remember to keep interactions engaging, informative, and pleasant, while providing the best possible assistance to the users. Here is the name of the user you are talking to: ";
    private static final String INSTRUCTIONS = "You are dAIv, a friendly and personable text message assistant created by Jaden. It's important to note that the current date and time provided above is what you should consider as 'now'. You are here to assist users with their queries and tasks. If the user encounters an error with the chatbot, remind them that they can use the command !report to report any issues. Your responses should never mention that you are an AI developed by OpenAI or use phrases like 'as an AI language model.' If a user asks to set a reminder or any similar task, you must carefully follow these steps: Start your response with .reminder{x} where x is the time since the Unix epoch (in seconds, make sure you include the { and } between the time). Do not ever respond with the process of how you calculated the Unix Timestamp. The calculations you do must be based off of the date provided to you at the beginnig of this prompt; for example, if the user says tomorrow, then they mean the day after the date provided, and the millisecond calculation you do with unix time should be based on the date provided. This part is for the system handling the response and should never be shared with the user. After .reminder{x}, write a brief 1-10 word synopsis of the reminder, followed by a | to indicate the end of the command. After the |, inform the user that they will be reminded to complete the task at the specified time. Be polite, engaging, and ensure that your responses are helpful and straightforward. Remember, you are here to make things easier and more enjoyable for the user!";

    static {
        try {
            assistantId = regexResponse(createAssistant(), "id");
            System.out.println("ID: " + assistantId);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException, NameNotFoundException {
        User j = new User(new PhoneNumber("2508809769"), "Jaden");

        System.out.println(sendAndReceive(j, "in 2 hours remind me to wash my hands"));
    }

    private static String addMessageToUserThread(User user, String message) throws NameNotFoundException {
        if(user.getThreadId() == null){ // create new thread if one doesn't exist
            user.setThreadId(regexResponse(createThread(), "id"));
        }

        addMessageToThread(user.getThreadId(), message);

        System.out.println(user.getThreadId());

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

    public static String sendAndReceive(User user, String message) {
        try {
            String response = retrieveFromRun(user, addMessageToUserThread(user, message));
            response = response.replace("\\\"", "'");
            response = response.replace("\\n", "\n");

            if(response.contains(".reminder{")){
                String reminderMessage = response.substring(response.indexOf('}') + 1, response.indexOf('|'));
                long timeSinceEpoch = Long.parseLong(response.substring(response.indexOf(".reminder{") + 10, response.indexOf('}')));
                System.out.println("reminder! message: " + reminderMessage + " | timeEpoch: " + timeSinceEpoch);

                Date expire = new Date();
                expire.setTime(timeSinceEpoch);
                Reminder reminder = new Reminder(user, expire, reminderMessage);

                //response = response.substring(response.indexOf(".reminder"), response.indexOf("|") + 1);
                //response = response.substring(0, response.indexOf(".reminder{")) + response.substring(response.indexOf("|") + 2);
                return "Attempting to set reminder...";
            }

            return response;
        } catch(Exception e) {
            return "Error";
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
        // implement the run creation logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/runs";
        String requestBody = "{"
                + "\"assistant_id\": \"" + assistantId + "\"," // this is where we can alter our instructions -- this gets run whenever we add a new message to the thread!
                + "\"instructions\": \"[CURRENT unix timestamp and date: " + System.currentTimeMillis() + " " + Event.timeFormat.format(new Date()) + " " + Event.dateFormat.format(new Date()) + "] " + INSTRUCTIONS +  " The user's name is " + user.getUserName() +  " and this is their prompt to you: \""
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
