import com.twilio.type.PhoneNumber;

import javax.naming.NameNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.regex.*;

class GPTAPI {

    private static final String API_KEY = "YOUR_CHATGPT_API_KEY";
    public static String assistantId;

    static {
        try {
            assistantId = regexResponse(createAssistant(), "id");
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //private static Pattern pattern = Pattern.compile("\"id\": \"([^\"]+)\"");
    public static void main(String[] args) throws InterruptedException, NameNotFoundException {
        User j = new User(new PhoneNumber("2508809769"), "Jaden");
        //User y = new User(new PhoneNumber("1"), "bob");
        System.out.println(sendAndReceive(j, "A uniform, rigid rod of length 2m lies on a horizontal surface. One end of the rod can pivot about an axis that is perpendicular to the rod and along the plane of the page. A 10N force is applied to the rod at its midpoint at an angle of 37 degrees. A second force F is applied to the free end of the rod so that the rod remains at rest. The magnitude of the torque produced by force F is most nearly? Also, tell me some recent news from Victoria, British Columbia."));
        // System.out.println(sendAndReceive(j, "What was the last thing I asked you?"));
        // System.out.println(sendAndReceive(y, "What was the last thing I asked you?"));
    }

    private static String addMessageToUserThread(User user, String message) throws NameNotFoundException {
        if(user.getThreadId() == null){ // create new thread if one doesn't exist
            user.setThreadId(regexResponse(createThread(assistantId), "id"));
            // addMessageToThread(assistantId, user.getThreadId(), "");
        }
        addMessageToThread(assistantId, user.getThreadId(), message);

        // returns the runID
        return regexResponse(createRun(assistantId, user.getThreadId(), user.getUserName()), "id");
    }

    private static String retrieveFromRun(User user, String runID) throws NameNotFoundException, InterruptedException {
        String response;
        for(int i = 0; i < 20; i++){
            response = regexResponse(pollRun(user.getThreadId(), runID), "status");
            Thread.sleep(550);
            if(response.equals("completed")){ // if the server has handled our request, return it
                return regexResponse(retrieveMessagesFromThread(user.getThreadId()), "value");
            }
        }
        return "There was an error processing this response.";
    }

    public static String sendAndReceive(User user, String message) throws NameNotFoundException, InterruptedException {
        //System.out.println("Sending message to ChatGPT API with threadId " + user.getThreadId());
        return retrieveFromRun(user, addMessageToUserThread(user, message));
    }

    private static String createAssistant() {
        // Implement the assistant creation logic here
        String url = "https://api.openai.com/v1/assistants";
        String requestBody = "{"
                + "\"instructions\": \"You are a personal physics tutor, specifically for AP Physics 1. Write and run code to answer physics questions.\","
                + "\"name\": \"Physics Tutor\","
                + "\"model\": \"gpt-4\""
                + "}";
        return sendPostRequest(url, requestBody);
    }

    private static String createThread(String assistantId) {
        // Implement the thread creation logic here
        String url = "https://api.openai.com/v1/threads";
        return sendPostRequest(url, "{}");
    }

    private static String regexResponse(String response, String filterParameter) throws NameNotFoundException {
        Pattern pattern = Pattern.compile("\"" + filterParameter + "\": \"([^\"]+)\"");
        Matcher match = pattern.matcher(response);

        if(!match.find()){
            throw new NameNotFoundException("Couldn't find regex with given filterParameter!");
        } else {
            // System.out.println(match.group(1)); for debugging purposes
            return match.group(1);
        }
    }

    private static void addMessageToThread(String assistantId, String threadId, String message) {
        // Implement the message addition logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/messages";
        String requestBody = "{\"role\": \"user\", \"content\": \"" + message + "\"}";
        sendPostRequest(url, requestBody);
    }

    private static String createRun(String assistantId, String threadId, String username) {
        // Implement the run creation logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/runs";
        String requestBody = "{"
                + "\"assistant_id\": \"" + assistantId + "\"," // this is where we can alter our instructions -- this gets run whenever we add a new message to the thread!
                + "\"instructions\": \"Please address the user as " + username +  ". The user has a premium account.\""
                + "}";
        String post = sendPostRequest(url, requestBody);
        return post;
    }

    private static String pollRun(String threadId, String runId){ // can probably offload lots of this to a get method
        try {
            URL url = new URL("https://api.openai.com/v1/threads/" + threadId + "/runs/" + runId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("OpenAI-Beta", "assistants=v1");

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
            connection.setRequestProperty("OpenAI-Beta", "assistants=v1");
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
            connection.setRequestProperty("OpenAI-Beta", "assistants=v1");
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
