import com.twilio.type.PhoneNumber;

public class User {
    public PhoneNumber phoneNumber;
    // change me important VVVV
    public String userName;

    public User(PhoneNumber phoneNum, String userN) {
        this.phoneNumber = phoneNum;
        this.userName = userN;
    }

    private void writeToFile() {

    }

    private void readFromFile() {

    }

    
}