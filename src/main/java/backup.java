import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.twilio.type.PhoneNumber;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class backup {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    // Method to save a list of User objects to a JSON file
    public static void saveUsersToJSON(ArrayList<User> users) {
        try (FileWriter writer = new FileWriter("users.json")) {
            gson.toJson(users, writer);
            System.out.println("User objects saved to users.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a User object to JSON

    public static void updateAndSaveUser(User newUser) {
        ArrayList<User> users = getUsersFromJSON();
        Iterator<User> iterator = users.iterator();
        if(!getUsersFromJSON().isEmpty()) {
            while (iterator.hasNext()) {
                User existingUser = iterator.next();
                if (existingUser.getUserName().equals(newUser.getUserName())) {
                    iterator.remove();
                }
            }
        }
        users.add(newUser);
        backup.saveUsersToJSON(users);
    }

    // Method to return a list of User objects from JSON file
    public static ArrayList<User> getUsersFromJSON() {
        ArrayList<User> users = new ArrayList<>();
        try (Reader reader = new FileReader("users.json")) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(reader, userListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }


    // Main method for testing purposes
    public static void main(String[] args) {
        User zachary = new User(new PhoneNumber("000000000"), "zachary");
        zachary.events.add(new ScheduledEvent(5000, new Task(){
            @Override
            public void execute(){
                System.out.println("beh");
            }
        }));
        ArrayList<User> users = new ArrayList<>();

        backup.updateAndSaveUser(zachary);
        ArrayList<User> loadedUsers = backup.getUsersFromJSON();
        for (User user : loadedUsers) {
            System.out.println("Loaded user: " + user.getUserName());
            System.out.println("Loaded number: " +user. getPhoneNumber());
        }
    }
}