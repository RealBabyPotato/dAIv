import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class backup {
    private JSONArray backup; // JSON array to hold all task objects

    public backup() {
        backup = new JSONArray();
    }


    // Method to add a task with message, scheduled event, category, list, and characteristics
    public void addToJSON(String message, String category, String list, String time) {
        // Create JSON objects for each task component
        JSONObject task = new JSONObject();
        JSONObject messageObj = new JSONObject();
        JSONObject timeObj = new JSONObject();
        JSONObject categoryObj = new JSONObject();
        JSONObject listObj = new JSONObject();

        // Set values for each component
        messageObj.put("message", message);
        timeObj.put("time", time);
        categoryObj.put("category", category);
        listObj.put("list", list);

        // Add each component to the task object
        task.put("message", messageObj);
        task.put(timeObj, time);
        task.put("category", categoryObj);
        task.put("list", listObj);

        // Add the task object to the backup array
        backup.add(task);
    }

    // Method to save backup to a JSON file
    public void saveBackupToJson(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(backup.toJSONString());
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
        taskManager.addToJSON("Sample Task", "Sample Category", "Sample List", "Sample Time");
        taskManager.saveBackupToJson("backup.json");

        // Loading backup from JSON
        taskManager.loadBackupFromJson("backup.json");
    }
}
//     public static void reset(){
//          webScraper.reset();
//          users.reset();
//          schedule.reset();
//          extractor.reset();
//     }
// }
