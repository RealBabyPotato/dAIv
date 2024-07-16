import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * Backs things up into users.json old-style.
 * @deprecated use {@backup.java} instead.
 */
@Deprecated
public class JSONManager{ // deprecated
    private String user;
    private int txt;
    private static JSONArray allUser = new JSONArray();

    public JSONManager(String user){
        this.user = user;
        txt = 0;
        // Initialize userObject and conversation arrays here
        JSONObject userObject = new JSONObject();
        JSONArray conversation = new JSONArray();
        userObject.put(user, conversation);
        allUser.add(userObject);
    }

    public String get_user() {
        return user;
    }

    public void addConversation(String userTxt, String gptTxt){
        JSONObject oneTxt = new JSONObject();
        oneTxt.put(userTxt, gptTxt);

        // Get userObject and conversation arrays
        JSONObject userObject = (JSONObject) allUser.get(0);
        JSONArray conversation = (JSONArray) userObject.get(user);

        // Add conversation to the conversation array
        JSONObject txtNumber = new JSONObject();
        txtNumber.put(txt, oneTxt);
        conversation.add(txtNumber);

        // Increment txt counter
        txt++;

        // Save to file
        saveToFile();
    }

    private void saveToFile() {
        try(FileWriter file = new FileWriter("backup_conversation.json", false)){
            file.write(allUser.toJSONString());
            file.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void read_File(String requestedUser, String fileName) throws org.json.simple.parser.ParseException{

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)){
            Object obj = parser.parse(reader);
            JSONArray allUsers = (JSONArray) obj;

            for (Object userObj : allUsers) {
                JSONObject userObject = (JSONObject) userObj;

                if (userObject.containsKey(requestedUser)) {
                    JSONArray conversation = (JSONArray) userObject.get(requestedUser);

                    for (Object convObj : conversation) {
                        JSONObject txtNumber = (JSONObject) convObj;

                        for (Object key : txtNumber.keySet()) {
                            JSONObject txt = (JSONObject) txtNumber.get(key);

                            for (Object userTxt : txt.keySet()) {
                                System.out.println(userTxt + ": " + txt.get(userTxt)); // ??????? 4 for statements?? i can't really fix this because i don't know tooooo much about the best way to approach this ;) -- jaden
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