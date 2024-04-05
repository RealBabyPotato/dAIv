import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BackUpTime {

    private List<JSONObject> tasks;

    public BackUpTime() {
        tasks = new ArrayList<>();
    }

    // Method to add a task with a message and time
    public void addTask(String message, LocalDateTime time) {
        JSONObject task = new JSONObject();
        task.put("request", message); // Adding the message to the task JSON
        task.put("time", time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // Converting time to String and adding to the task JSON
        tasks.add(task); // Adding the task JSON to the tasks list
    }

    // Method to save tasks to a JSON file
    public void saveTasksToJson(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            JSONArray taskList = new JSONArray();
            taskList.addAll(tasks);
            file.write(taskList.toJSONString());
            System.out.println("Tasks saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load tasks from a JSON file
    public void loadTasksFromJson(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            JSONParser parser = new JSONParser();
            JSONArray taskList = (JSONArray) parser.parse(reader);
            tasks.clear();
            tasks.addAll(taskList);
            System.out.println("Tasks loaded from " + filename);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Main method for testing purposes
    public static void main(String[] args) {
        BackUpTime taskManager = new BackUpTime(); // Creating an instance of BackUpTime

        // Example usage: Adding a task and saving it to JSON
        taskManager.addTask("Task 1", LocalDateTime.now()); // Adding a task with a message and current time
        taskManager.saveTasksToJson("tasks.json"); // Saving tasks to a JSON file
    }
}
