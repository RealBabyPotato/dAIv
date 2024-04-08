import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
okay gang here's a good way to begin going about storing user conversations, i outlined some in FileReadWrite
this was my first idea so there is probably holes, but this should *roughly* be the result? - jaden

{
  "user": "John Doe",
  "messages": [
    {
      "message": "Hello!",
      "reply": "Hi there!",
      "time": "2024-04-08T10:00:00"
    },
    {
      "message": "How are you?",
      "reply": "I'm doing well, thank you!",
      "time": "2024-04-08T10:05:00"
    },
    {
      "message": "What are you up to?",
      "reply": "Just working on some tasks.",
      "time": "2024-04-08T10:10:00"
    }
  ]
}

subject to change...
 */

public class BackUp {

    private List<JSONObject> tasks;
    private List<JSONObject> times;
    private List<JSONObject> catagories;
    private List<JSONObject> lists;
    private List<JSONObject> characteristics;

    public BackUp() {
        tasks = new ArrayList<>();
        times = new ArrayList<>();
        catagories = new ArrayList<>();
        lists = new ArrayList<>();
        characteristics = new ArrayList<>();
    }

    // Method to add a task with a message and time
    public void addTask(String message, String TimeObject, String CatagoryObject, String ListObject, String CharacteristicsObject) {
        JSONObject task = new JSONObject();
        JSONObject time = new JSONObject();
        JSONObject catagory = new JSONObject();
        JSONObject list = new JSONObject();
        JSONObject characteristic = new JSONObject();
        task.put("message", message); // Adding the message to the task JSON
        time.put("time", TimeObject); // Converting time to String and adding to the task JSON
        catagory.put("Catagory", CatagoryObject);
        list.put("List", ListObject);
        characteristic.put("Characteristics", CharacteristicsObject);
        tasks.add(task); // Adding the task JSON to the tasks list
        times.add(time);
        catagories.add(catagory);
        lists.add(list);
        characteristics.add(characteristic);
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

    // Method to save times to a JSON file
    public void saveTimesToJson(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            JSONArray timeList = new JSONArray();
            timeList.addAll(times);
            file.write(timeList.toJSONString());
            System.out.println("Times saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save categories to a JSON file
    public void saveCatagoriesToJson(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            JSONArray catagoryList = new JSONArray();
            catagoryList.addAll(catagories);
            file.write(catagoryList.toJSONString());
            System.out.println("Categories saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save lists to a JSON file
    public void saveListsToJson(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            JSONArray listList = new JSONArray();
            listList.addAll(lists);
            file.write(listList.toJSONString());
            System.out.println("Lists saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save characteristics to a JSON file
    public void saveCharacteristicsToJson(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            JSONArray characteristicList = new JSONArray();
            characteristicList.addAll(characteristics);
            file.write(characteristicList.toJSONString());
            System.out.println("Characteristics saved to " + filename);
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
        BackUp taskManager = new BackUp(); // Creating an instance of BackUpTime

        // Example usage: Adding a task and saving it to JSON
        taskManager.addTask("Task 1", "a", "b", "c", "d"); // Adding a task with a message and current time
        taskManager.saveTasksToJson("tasks.json"); // Saving tasks to a JSON file
        taskManager.saveTimesToJson("times.json"); // Saving times to a JSON file
        taskManager.saveCatagoriesToJson("categories.json"); // Saving categories to a JSON file
        taskManager.saveListsToJson("lists.json"); // Saving lists to a JSON file
        taskManager.saveCharacteristicsToJson("characteristics.json"); // Saving characteristics to a JSON file

        taskManager.loadTasksFromJson("tasks.json"); // Saving tasks to a JSON file
        taskManager.loadTasksFromJson("times.json"); // Saving times to a JSON file
        taskManager.loadTasksFromJson("categories.json"); // Saving categories to a JSON file
        taskManager.loadTasksFromJson("lists.json"); // Saving lists to a JSON file
        taskManager.loadTasksFromJson("characteristics.json"); // Saving characteristics to a JSON file
    }
}
