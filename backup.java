import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twilio.type.PhoneNumber;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class backup {
    private JSONArray backup; // JSON array to hold all task objects

    public backup() {
        backup = new JSONArray();
    }

    // Method to add a task with message, scheduled event, category, list, and characteristics
    public void addToJSON(String message, String reply, ScheduledEvent scheduledEvent, User user) {
        // Create JSON objects for each component
        JSONObject client = new JSONObject();
        JSONObject messages = new JSONObject();
        JSONObject task = new JSONObject();
        JSONObject userObj = new JSONObject();
        JSONObject messageObj = new JSONObject();
        JSONObject scheduledEventObj = new JSONObject();
        JSONObject listObj = new JSONObject();

        // Set values for each component
        userObj.put("phone number", user.phoneNumber.toString());
        userObj.put("thread id", user.getThreadId());
        userObj.put("username", user.getUserName());
        messageObj.put("message", message);
        messageObj.put("reply", reply);
        messageObj.put("time", scheduledEvent.getTime());
        scheduledEventObj.put("task", scheduledEvent.getRequest());
        scheduledEventObj.put("time", scheduledEvent.getTime());

        // Add each component to the task object
        client.put("user", userObj);
        messages.put("messages", messageObj);
        messages.put("reminders", scheduledEventObj);


        // Add the task object to the backup array
        backup.add(userObj);
        backup.add(messages);
    }

    // Method to save backup to a JSON file
    public void saveBackupToJson(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(backup);
            file.write(jsonOutput);
            System.out.println("Backup saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load backup from a JSON file
    public void loadBackupFromJson(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            JSONParser parser = new JSONParser();
            backup = (JSONArray) parser.parse(reader);
            System.out.println("Backup loaded from " + filename);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Main method for testing purposes
    public static void main(String[] args) {
        backup taskManager = new backup();

        // Example usage: Adding tasks and saving them to JSON
        ScheduledEvent eventA = new ScheduledEvent("Do homework", Calendar.getInstance());
        User sampleUser = new User(new PhoneNumber("0000000000"), "zachary");
        taskManager.addToJSON("This is a draft of what the final JSON file might look like.", "Both messages and replies will be stored, as well as the time.", eventA, sampleUser);
        taskManager.saveBackupToJson("backup.json");

        // Loading backup from JSON
        taskManager.loadBackupFromJson("backup.json");
    }
}
