import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twilio.type.PhoneNumber;

import java.util.ArrayList;
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
    ArrayList<Event> events = new ArrayList<Event>();
    
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
        backup.updateAndSaveUser(this);
    }

    public static User registerUser(PhoneNumber number){
        // here we can ask them to tell us their name or something so that we can put in userName; this is just a big setup process. 
        // note that this will be called in SMSHandler.java when we don't recognize an incoming user
        // so it may be useful to use context from there.

        User incomingUser = new User(number, "in setup phase"); // this will automatically register our user in Main.registeredUsers.
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
        return getPhoneNumber().toString();
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

    public <T extends Event> void addEvent(T event){
        if(event.getClass() == Event.class){
            System.out.println("Attempting to add an Event superclass through 'addEvent'! This is not allowed!");
        }

        if(event.getClass() == Reminder.class){
            addReminder((Reminder)event);
            return;
        }
    }

    private void addReminder(Reminder e){
        events.add(e);
        System.out.println("Adding reminder! List of user's reminders: ");
        for(Event v : events){
            System.out.println(v.expiryTime);
        }
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

    // Twillio Send message
    public void message(String content){
        if(content.equals("")) { System.out.println("DEBUG: attempting to send message with empty string, returning"); return; }
        TwilioSendMessageExample.messageUser(this, content);
    }

    public static User getUserWithNumberString(String num){

        for(User u : Main.RegisteredUsers){
            if(u.getPhoneNumberAsString().equals(num)){
                return u;
            }
        }

        return null;
    }

    // Reset and Reboot
    public static void PopulateUsers() {
        /*
        From conversations directory, loop through all json files
        and create new User objects with appropriate information.
         */
        ArrayList<User> users = backup.getUsersFromJSON();

        for(User u : users){
            for(Event event : u.events){ // pair every event to it's owner
                if(event instanceof Reminder){
                    event.owner = u;
                    ((Reminder) event).begin(); // we know this is a reminder
                }
                //System.out.println("Loaded event with username: " + event.owner.getUserName());
            }
        }

        Main.RegisteredUsers.addAll(users);
    }

    public static void main(String[] args) {
        PopulateUsers();

        System.out.println(getUserWithNumberString("2508809769").getThreadId());
    }
}
