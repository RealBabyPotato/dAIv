import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class GPTAPI{
    public static void main(String... args) {
        // we'll use Mr. H's token here
        String token = "YOUR_CHATGPT_API_KEY";

        OpenAiService service = new OpenAiService(token);

        // this is how we make a basic request. this should be pretty intuitive -- it uses an old version of gpt 3.5 tho!
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Tell me a joke.")
                .model("gpt-3.5-turbo-instruct")
                .echo(true)
                .build();

        // this is what we do with the output. for now we just use a method reference to print the response out to the console.
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);


        // this is a more manual approach, but i think it'll be the way forward. this is gpt-4 and we can customize endpoints, use assistants, and so on.
        System.out.println(prompt("Tell me a joke."));
    }

    // this is where we are going to be making our other methods that interact with chat.
    // @ethan & @sophia, take a look at how to use the assistants api.

    public static String prompt(String prompt){
        String url = "https://api.openai.com/v1/chat/completions"; // assistants api instead later
        String apiKey = "YOUR_CHATGPT_API_KEY";
        String model = "gpt-4";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Beta", "assistants=v1");

            /* The request body
            String body;
            if(prompt != ""){
                body = "{\"role\": \"user\", \"content\": \"" + prompt + "\"}";
            } else{
                body = "{\"model\": \"" + model + "\", \"name\": \"Categorizer\", \"instructions\": \"You are a word categorizer. When given a word, you say what the first letter in that word is.\"}";
            }*/
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }
}