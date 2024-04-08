import com.twilio.type.PhoneNumber;

import java.io.File;

public class User {
    public PhoneNumber phoneNumber;

    private String userName;

    public User(PhoneNumber phoneNum, String userN) {
        this.phoneNumber = phoneNum;
        this.userName = userN;
        Main.RegisteredUsers.add(this);
    }

    public User(PhoneNumber phoneNum){
        this.phoneNumber = phoneNum;
        Main.RegisteredUsers.add(this);
    }

    public String getUserName(){
        return userName;
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

    public void message(String content){
        TwilioSendMessageExample.messageUser(this, content);
        // this.writeToFile(content); uncomment this when we fix file io
    }

    
}