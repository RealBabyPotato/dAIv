import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.twilio.type.PhoneNumber;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class backup {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            //.registerTypeAdapter(ScheduledEvent.class, new ScheduledEventTypeAdapter())
            .registerTypeAdapter(Event.class, new EventDeserializer())
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    // Method to save a list of User objects to a JSON file
    private static void saveUsersToJSON(ArrayList<User> users) {
        try (FileWriter writer = new FileWriter("users.json", false)) {
            gson.toJson(users, writer);
            System.out.println("Updated and saved users.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // I think this is a little bit cleaner but lmk if it doesn't work as intended -- jaden
    public static void updateAndSaveUser(User newUser) {
        ArrayList<User> users = getUsersFromJSON();
        try{
            users.removeIf(existingUser -> existingUser.getPhoneNumberAsString().equals(newUser.getPhoneNumberAsString())); // if nothing changed, do not update this user; updates

        } catch (NullPointerException e){
            System.out.println("Creating new user in users.json");
        }
        
        users.add(newUser); // adds
        
        backup.saveUsersToJSON(users);
    }

    // Method to return a list of User objects from JSON file
    public static ArrayList<User> getUsersFromJSON() {
        ArrayList<User> users = new ArrayList<User>();

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
        User.PopulateUsers();

        for (User user : Main.RegisteredUsers) {
            System.out.println("Loaded user: " + user.getUserName());
            System.out.println("Loaded number: " + user.getPhoneNumber());
            System.out.println("Loaded threadID: " + user.getThreadId());
        }
    }
}