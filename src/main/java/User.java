import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twilio.type.PhoneNumber;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.io.File;
import java.util.Objects;


public class User {
    // Instance Variables

    //@Expose // This tag is for the backup group
    private PhoneNumber phoneNumber;

    @Expose
    private String phoneNumberString;

    @Expose
    private String userName;

    @Expose
    @SerializedName("events")
    ArrayList<ScheduledEvent> events = new ArrayList<ScheduledEvent>();
    
    @Expose
    private String threadId;

    @Expose
    private boolean isInSetup = true;

    @Expose
    private int setupPhase = 0;

    public User(PhoneNumber phoneNum, String userN) {
        this.phoneNumber = phoneNum;
        this.userName = userN;
        this.phoneNumberString = phoneNum.toString();
        Main.RegisteredUsers.add(this);
    }

    public static User registerUser(PhoneNumber number){
        // here we can ask them to tell us their name or something so that we can put in userName; this is just a big setup process. 
        // note that this will be called in SMSHandler.java when we don't recognize an incoming user
        // so it may be useful to use context from there.

        User incomingUser = new User(number, null); // this will automatically register our user in Main.registeredUsers.
        backup.updateAndSaveUser(incomingUser); // this backs up our new user.

        return incomingUser;
    }

    public static User registerUser(PhoneNumber number, String firstMessage){
        System.out.println("DEBUG: registered user with first message: " + firstMessage);
        return registerUser(number);
    }

    public boolean getIsInSetup(){
        return isInSetup;
    }

    public void setIsInSetup(boolean val){
        isInSetup = val;
        backup.updateAndSaveUser(this);
    }

    public int getSetupPhase(){
        return setupPhase;
    }

    public void setSetupPhase(int val){
        setupPhase = val;
        backup.updateAndSaveUser(this);
    }
    // Accessor Methods
    public String getUserName(){
        return userName;
    }

    public PhoneNumber getPhoneNumber(){
        if(this.phoneNumber == null){
            return new PhoneNumber(phoneNumberString);
        }

        return phoneNumber;
    }

    public String getPhoneNumberAsString(){
        return  phoneNumber.toString();
    }

    public String getThreadId() { return threadId; }

    public void setThreadId(String id){
        this.threadId = id;
        backup.updateAndSaveUser(this);
    }

    public void setUsername(String username){
        this.userName = username;
        backup.updateAndSaveUser(this);
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
        // PopulateUsers();
        User b = new User(new PhoneNumber("25"), "bob");
        backup.updateAndSaveUser(b);
    }
}
