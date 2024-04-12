import com.twilio.type.PhoneNumber;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.io.File;


public class User {
    // Instance Variables
    public PhoneNumber phoneNumber;
    private String userName;
    ArrayList<ScheduledEvent> events = new ArrayList<ScheduledEvent>();
    private String threadId;
    private int testInt;

    // Constructor Methods
    public User(PhoneNumber phoneNum, String userN, String messages) {

    }

    public User(PhoneNumber phoneNum, String userN) {
        this.phoneNumber = phoneNum;
        this.userName = userN;
        Main.RegisteredUsers.add(this);
    }

    // Accessor Methods
    public String getUserName(){
        return userName;
    }

    public String getThreadId() {
        return threadId;
    }


    public void setThreadId(String id){
        this.threadId = id;
    }


    // Utility
    public String toString() {
        return "PN:" + phoneNumber.toString() + "," + "userName:" + userName + "," + "threadID:" + threadId;
    }

    public void writeToFile(String message) {
        String reply = "This is where our ChatGPT reply will be handled.";
        FileReadWrite.addToConversation(this, message, reply);
    }

    public void writeToFile(String message, String overrideReply){
        FileReadWrite.addToConversation(this, message, overrideReply);
    }

    public void readFromFile() {
        FileReadWrite.readConversation(this);
    }

    // Twillio Send message
    public void message(String content){
        TwilioSendMessageExample.messageUser(this, content);
        // this.writeToFile(content); uncomment this when we fix file io
    }

    // Reset and Reboot
    public static void PopulateUsers() {
        /*
        From conversations directory, loop through all json files
        and create new User objects with appropriate information.
         */

//        String username;
//        String[] messages;
//        PhoneNumber phonenumber;
//
//        Main.RegisteredUsers.add(new User(username, phonenumber, messages));

    }

    public static void main(String[] args) {
        PopulateUsers();
    }


}