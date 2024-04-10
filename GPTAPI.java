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
    //private static Pattern pattern = Pattern.compile("\"id\": \"([^\"]+)\"");
    public static void main(String[] args) throws InterruptedException, NameNotFoundException {
        String assistantId = regexResponse(createAssistant(), "id");
        String threadId = regexResponse(createThread(assistantId), "id");
        addMessageToThread(assistantId, threadId, "I need to solve the equation `3x + 11 = 14`. Can you help me?");
        String runId = regexResponse(createRun(assistantId, threadId), "id");

        while(true){
            Thread.sleep(3000);
            regexResponse(pollRun(threadId, runId), "status");
            // regexResponse(retrieveMessagesFromThread(threadId), "id");
            System.out.println(retrieveMessagesFromThread(threadId));
        }
    }

    private static String createAssistant() {
        // Implement the assistant creation logic here
        String url = "https://api.openai.com/v1/assistants";
        String requestBody = "{"
                + "\"instructions\": \"You are a personal math tutor. Write and run code to answer math questions.\","
                + "\"name\": \"Math Tutor\","
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
            System.out.println(match.group(1));
            return match.group(1);
        }
    }

    private static void addMessageToThread(String assistantId, String threadId, String message) {
        // Implement the message addition logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/messages";
        String requestBody = "{\"role\": \"user\", \"content\": \"" + message + "\"}";
        sendPostRequest(url, requestBody);
    }

    private static String createRun(String assistantId, String threadId) {
        // Implement the run creation logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/runs";
        String requestBody = "{"
                + "\"assistant_id\": \"" + assistantId + "\","
                + "\"instructions\": \"Please address the user as Jane Doe. The user has a premium account.\""
                + "}";
        String post = sendPostRequest(url, requestBody);
        //System.out.println(post);
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
            URL url = new URL("https://api.openai.com/v1/threads/" + threadId);
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
