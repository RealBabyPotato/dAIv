import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.regex.*;

class GPTAPI {

    private static final String API_KEY = "YOUR_CHATGPT_API_KEY"; // Replace with your actual API key

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\"id\": \"([^\"]+)\"");

        String assistantId = createAssistant();
        Matcher match = pattern.matcher(assistantId);
        if(!match.find()){
            System.out.println("Couldn't find assistant id");
            return;
        } else {
            assistantId = match.group(1);
            System.out.println("assistant ID: " + assistantId);
        }

        String threadId = createThread(assistantId);
        match = pattern.matcher(threadId);
        if(!match.find()){
            System.out.println("Couldn't find assistant id");
            return;
        } else {
            threadId = match.group(1);
            System.out.println("thread ID: " + threadId);
        }
        addMessageToThread(assistantId, threadId, "I need to solve the equation `3x + 11 = 14`. Can you help me?");
        createRun(assistantId, threadId);
    }

    private static String createAssistant() {
        // Implement the assistant creation logic here
        String url = "https://api.openai.com/v1/assistants";
        String requestBody = "{"
                + "\"instructions\": \"You are a personal math tutor. Write and run code to answer math questions.\","
                + "\"name\": \"Math Tutor\","
                // + "\"tools\": [{\"type\": \"code_interpreter\"}],"
                + "\"model\": \"gpt-4\""
                + "}";
        return sendPostRequest(url, requestBody);
    }

    private static String createThread(String assistantId) {
        // Implement the thread creation logic here
        String url = "https://api.openai.com/v1/threads";
        return sendPostRequest(url, "{}");
    }

    private static void addMessageToThread(String assistantId, String threadId, String message) {
        // Implement the message addition logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/messages";
        String requestBody = "{\"role\": \"user\", \"content\": \"" + message + "\"}";
        sendPostRequest(url, requestBody);
    }

    private static void createRun(String assistantId, String threadId) {
        // Implement the run creation logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/runs";
        String requestBody = "{"
                + "\"assistant_id\": \"" + assistantId + "\","
                + "\"instructions\": \"Please address the user as Jane Doe. The user has a premium account.\""
                + "}";
        String post = sendPostRequest(url, requestBody);
        System.out.println(post);
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
