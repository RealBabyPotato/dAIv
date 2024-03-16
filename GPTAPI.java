import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.client.

class GPTAPI{
    public static void main(String... args) {
        // we'll use Mr. H's token here
        String token = "TOKEN";

        OpenAiService service = new OpenAiService(token);

        // this is how we make a basic request. this should be pretty intuitive.
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Hola! My name is bob.")
                .model("gpt-3.5-turbo-0125")
                .echo(true)
                .build();

        // this is what we do with the output. for now we just use a method reference to print the response out to the console.
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }

    // this is where we are going to be making our other methods that interact with chat.
    // @ethan & @sophia, take a look at how to use the assistants api.
}