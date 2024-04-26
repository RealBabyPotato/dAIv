import com.google.gson.annotations.Expose;
import com.twilio.type.PhoneNumber;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.io.File;
import java.util.Objects;


public class User {
    // Instance Variables
    @Expose // This tag is for the backup group
    public PhoneNumber phoneNumber;
    @Expose
    private String userName;
    @Expose
    ArrayList<ScheduledEvent> events = new ArrayList<ScheduledEvent>();
    @Expose
    private String threadId;

    public User(PhoneNumber phoneNum, String userN) {
        this.phoneNumber = phoneNum;
        this.userName = userN;
        Main.RegisteredUsers.add(this);
    }

    //public

    // Accessor Methods
    public String getUserName(){
        return userName;
    }

    public PhoneNumber getPhoneNumber(){
        return phoneNumber;
    }

    public String getPhoneNumberAsString(){
        return  phoneNumber.toString();
    }

    public String getThreadId() { return threadId; }

    public void setThreadId(String id){
        this.threadId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(userName, user.userName);
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
        ArrayList<User> users = backup.getUsersFromJSON();
        Main.RegisteredUsers.addAll(users);
    }

    public static void main(String[] args) {
        PopulateUsers();
    }
}
