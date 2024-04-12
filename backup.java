import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.twilio.type.PhoneNumber;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class backup {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Method to save a list of User objects to a JSON file
    public void saveUsersToJSON(ArrayList<User> users) {
        try (FileWriter writer = new FileWriter("users.json")) {
            gson.toJson(users, writer);
            System.out.println("User objects saved to users.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a User object to JSON
    public void addUserToJSON(User user) {
        ArrayList<User> users = getUsersFromJSON();
        if(!users.contains(user)) {
            users.add(user);
            saveUsersToJSON(users);
        }
    }

    // Method to return a list of User objects from JSON file
    public ArrayList<User> getUsersFromJSON() {
        ArrayList<User> users = new ArrayList<>();
        try (Reader reader = new FileReader("users.json")) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            users = gson.fromJson(reader, userListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Main method for testing purposes
    public static void main(String[] args) {
        backup backup = new backup();
        User zachary = new User(new PhoneNumber("000000000"), "zachary");
        User hanson = new User(new PhoneNumber("123456789"), "hanson");
        backup.addUserToJSON(zachary);
        backup.addUserToJSON(hanson);
        ArrayList<User> loadedUsers = backup.getUsersFromJSON();
        for (User user : loadedUsers) {
            System.out.println("Loaded user: " + user.getUserName());
        }
    }
}
