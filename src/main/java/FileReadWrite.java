import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Calendar;

public class FileReadWrite {

    public static void addToConversation(User client, String message, String reply) {
        if (client.getUserName() == null) {
            System.out.println("Can't create a conversation for user with null username!");
            return;
        }

        File fileName = new File("conversations/" + client.getUserName() + "Conversation.json");
        JSONObject conversation = null;

        try {
            // try to read existing conversation
            FileReader reader = new FileReader(fileName);
            JSONParser parser = new JSONParser();
            conversation = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (IOException | ParseException e) {
            // create a new conversation if one doesn't already exist
            conversation = new JSONObject();
            conversation.put("Messages", new JSONArray());
            // conversation.put("time", time);
        }

        // add the new message to the conversation
        JSONObject newMessage = new JSONObject();
        newMessage.put("Message", message);
        newMessage.put("Reply", reply);
        newMessage.put("Time", "" + Calendar.getInstance().getTime() + ""); // change to a calendar object maybe? or whatever dateTime people and file io people agree on
        ((JSONArray) conversation.get("Messages")).add(newMessage);

        // write the updated conversation back to the file
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(conversation.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readConversation(User client) {
        File fileName = new File("conversations/" + client.getUserName() + "Conversation.json");

        try {
            FileReader reader = new FileReader(fileName);
            JSONParser parser = new JSONParser();
            JSONObject conversation = (JSONObject) parser.parse(reader);
            JSONArray messages = (JSONArray) conversation.get("Messages");

            System.out.println("\nConversation for user: " + client.getUserName());

            // print each message in the conversation
            for (Object obj : messages) {
                JSONObject message = (JSONObject) obj;
                System.out.println("userMessage: " + message.get("Message") + " || reply: " + message.get("Reply"));
            }

            reader.close();
        } catch (IOException | ParseException e) {
            if(client.getUserName() == null){
                System.out.println("Can't read file for this client - client's username is null (perhaps this user doesn't have a username?)");
            } else {
                System.out.println("No conversation found for user " + client.getUserName());
            }
        }
    }
}
