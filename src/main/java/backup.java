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
            //.registerTypeAdapter(ScheduledEvent.class, new ScheduledEventTypeAdapter())
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

           /* for(int i = 0; i < users.size(); i++){
                if(users.get(i).getPhoneNumberAsString().equals(newUser.getPhoneNumberAsString())){

                }
            }*/

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
        //User jaden = new User(new PhoneNumber("2508809769"), "Jaden");

        /*jaden.events.add(new ScheduledEvent(5000, new Task(){
            @Override
            public void execute(){
                System.out.println("beh");
            }
        }));*/

        //jaden.events.add(new RepeatedEvent(1000, "asdf"));
        //jaden.events.add(new RepeatedEvent(2500, "tadaloo"));

        //backup.updateAndSaveUser(jaden);

        ArrayList<User> loadedUsers = backup.getUsersFromJSON();

        for (User user : loadedUsers) {
            System.out.println("Loaded user: " + user.getUserName());
            System.out.println("Loaded number: " + user.getPhoneNumber());
            System.out.println("Loaded threadID: " + user.getThreadId());
            for(ScheduledEvent event : user.events){
                System.out.println("Loaded event with message: " + event.getMessage());
            }
        }
    }
}