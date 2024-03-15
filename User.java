import com.twilio.type.PhoneNumber;

public class User {
    public PhoneNumber phoneNumber;

    private String userName;

    public User(PhoneNumber phoneNum, String userN) {
        this.phoneNumber = phoneNum;
        this.userName = userN;
    }

    public String getUserName(){
        return userName;
    }

    public void writeToFile(String message) {
        String reply = "This is where our ChatGPT reply will be handled.";
        FileReadWrite.addToConversation(this, message, reply);
    }

    public void readFromFile() {
        FileReadWrite.readConversation(this);
    }

    
}