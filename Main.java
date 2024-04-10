import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

import java.util.ArrayList;

class Main{

    Message mostRecentMessage = null;
    public static ArrayList<User> RegisteredUsers = new ArrayList<User>(); // not sure if we should keep this...

    public static void main(String[] args){
        Twilio.init(TwilioSendMessageExample.ACCOUNT_SID, TwilioSendMessageExample.AUTH_TOKEN);

        // this is how we register a new user atm
        User jaden = new User(new PhoneNumber("2508809769"), "Jaden");
        // User ethan = new User(new PhoneNumber("7785334028"));

        // this is how we can send a message to a user
        jaden.message("asdf");
        jaden.writeToFile("Test");
        jaden.readFromFile();
    }
}
