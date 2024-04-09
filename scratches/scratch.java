package scratches;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

class Scratch {
    public static void main(String[] args) {
        try {
            read_File("scratch_1.java");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static void read_File(String requestedUser) throws org.json.simple.parser.ParseException{

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("backup_conversation.json")){
            Object obj = parser.parse(reader);
            JSONArray allUsers = (JSONArray) obj;

            for (Object userObj : allUsers) {
                JSONObject userObject = (JSONObject) userObj;

                if (userObject.containsKey(requestedUser)) {
                    JSONArray conversation = (JSONArray) userObject.get(requestedUser);
                    System.out.println("Conversation for user " + requestedUser + ":");

                    for (Object convObj : conversation) {
                        JSONObject txtNumber = (JSONObject) convObj;

                        for (Object key : txtNumber.keySet()) {
                            JSONObject txt = (JSONObject) txtNumber.get(key);

                            for (Object userTxt : txt.keySet()) {
                                System.out.println(userTxt + ": " + txt.get(userTxt));
                            }
                        }

                    }

                    return;
                }
            }

            System.out.println("User " + requestedUser + " not found.");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}