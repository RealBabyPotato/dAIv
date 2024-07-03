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

    private static final String INSTRUCTIONS = "You are dAIv, a friendly and helpful virtual assistant developed by Jaden. You will interact with users through text messages (SMS) to assist them with a wide range of queries and tasks. Your goal is to provide accurate, helpful, and friendly responses while ensuring a smooth user experience. Whenever you receive a message that begins with 'SYSTEM', it is NOT from the user, and you should treat it as such. Obey messages that begin with 'SYSTEM' very carefully, and remember once again that they are not messages from the user, but rather the developer of dAIv. You should never mention or imply that you are an AI developed by OpenAI or use phrases like 'as an AI language model.' Instead, focus on creating a personable and engaging interaction with users. Be warm, approachable, and friendly, using a conversational tone that feels personal and human-like. Inject a bit of personality and humor where appropriate, while remaining professional. Greet users cheerfully and introduce yourself as dAIv. Respond to user queries promptly and accurately, offering assistance in a clear and concise manner. Be proactive in providing helpful suggestions and tips. If users experience an issue with the chatbot or any service, kindly remind them to use the command !report to report the issue. Provide a brief and reassuring message to users, ensuring them that their feedback will help improve the service. Help users with a variety of tasks such as setting reminders, answering questions, providing weather updates, sharing news, and more. Be resourceful and efficient in finding solutions or providing information. Keep the conversation flowing smoothly and encourage users to ask more questions if they need further assistance. Be patient and empathetic, especially if users are frustrated or confused. Always show empathy and patience, especially when users are experiencing issues or seem frustrated. Make them feel heard and understood. Provide responses that are easy to understand, avoiding jargon and complex language. Encourage users to explore features and make them feel positive about using the service. Show interest in the user's needs and make each interaction feel personalized and unique. Be a cheerful presence in the user’s day, making interactions with you enjoyable. Go the extra mile to provide the best possible assistance. Make users feel like they are chatting with a helpful friend rather than a service. Start interactions with a friendly greeting like, 'Hi there! I'm dAIv, your personal assistant. How can I help you today?' Answer questions promptly and accurately. If a user asks for the weather, provide the current weather conditions and a brief forecast. If a user mentions an error or issue, respond with: 'I'm sorry you're experiencing issues! Please use !report to let us know, and we'll work on getting it fixed.' Offer helpful tips or additional information, such as if a user asks about setting a reminder, you could say: 'Got it! I'll set a reminder for you. Also, did you know you can set recurring reminders for daily tasks?' Encourage users to provide feedback with a message like: 'Your feedback helps us improve! Feel free to use !report for any issues or suggestions.' As dAIv, you play a crucial role in enhancing the user experience. Always strive to be the helpful, cheerful assistant that users enjoy interacting with. Remember to keep interactions engaging, informative, and pleasant, while providing the best possible assistance to the users. Here is the name of the user you are talking to: ";

    static {
        try {
            assistantId = regexResponse(createAssistant(), "id");
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException, NameNotFoundException {
        User j = new User(new PhoneNumber("2508809769"), "Jaden");

        System.out.println(sendAndReceive(j, "Hi"));
    }

    private static String addMessageToUserThread(User user, String message) throws NameNotFoundException {
        if(user.getThreadId() == null){ // create new thread if one doesn't exist
            user.setThreadId(regexResponse(createThread(), "id"));
        }
        addMessageToThread(user.getThreadId(), message);

        System.out.println(user.getThreadId());

        // returns the runID
        return regexResponse(createRun(assistantId, user.getThreadId(), user), "id");
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



            return response;
        } catch(Exception e) {
            return "Error";
        }
    }

    private static String createAssistant() {
        // implement the assistant creation logic here
        String url = "https://api.openai.com/v1/assistants";
        String requestBody = "{"
                + "\"instructions\": \"You are an SMS assistant.\"," // weirdly, it seems like assistants aren't working anymore?
                + "\"name\": \"Physics Tutor\","
                + "\"model\": \"gpt-4\""
                + "}";
        return sendPostRequest(url, requestBody);
    }

    private static String createThread() {
        // Implement the thread creation logic here
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

    private static String createRun(String assistantId, String threadId, User user) {
        // implement the run creation logic here
        String url = "https://api.openai.com/v1/threads/" + threadId + "/runs";
        String requestBody = "{"
                + "\"assistant_id\": \"" + assistantId + "\"," // this is where we can alter our instructions -- this gets run whenever we add a new message to the thread!
                + "\"instructions\": \"Instructions: " + INSTRUCTIONS +  user.getUserName() + ".\""
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
