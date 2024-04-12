import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.twilio.type.PhoneNumber;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class backup {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Method to save a User object to a JSON file
    public void saveUserToJSON(User user) {
        String jsonUser = gson.toJson(user);
        try (FileWriter writer = new FileWriter("user.json")) {
            writer.write(jsonUser);
            System.out.println("User object saved to user.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a User object to JSON
    public void addToJSON(User user) {
        saveUserToJSON(user);
    }

    // Method to return a User object from JSON file
    public User returnUser(String file) throws FileNotFoundException {
        return gson.fromJson(new JsonReader(new FileReader(file)), User.class);
    }

    // Main method for testing purposes
    public static void main(String[] args) throws FileNotFoundException {
        backup taskManager = new backup();
        User sampleUser = new User(new PhoneNumber("123456789"), "zachary");
        taskManager.addToJSON(sampleUser);
        System.out.print(taskManager.returnUser("user.json").getUserName());
    }
}
