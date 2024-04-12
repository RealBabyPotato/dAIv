import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;

class Main{

    Message mostRecentMessage = null;
    public static ArrayList<User> RegisteredUsers = new ArrayList<User>(); // not sure if we should keep this...

    public static void main(String[] args) throws NameNotFoundException, InterruptedException {
        Twilio.init(TwilioSendMessageExample.ACCOUNT_SID, TwilioSendMessageExample.AUTH_TOKEN);

        // this is how we register a new user atm
        User jaden = new User(new PhoneNumber("2508809769"), "Jaden");
        User ethan = new User(new PhoneNumber("7785334028"), "Ethan");

        // here is how we can use both twilio and chatgpt to send a message from chatgpt through twilio through our user! very cool!
        //jaden.message(GPTAPI.sendAndReceive(jaden, "How much wood would a woodchuck chuck if a woodchuck could chuck wood?").replaceAll("\\\\n", "\n")); // the replaceAll here just makes the \n function properly
        //jaden.message("guess who has written a script for the server and will now make a working console for it");
        // System.out.println(GPTAPI.sendAndReceive(ethan, "What was the last thing I asked you? Also, what is my name?"));
        // System.out.println(GPTAPI.sendAndReceive(jaden, "What was the last thing I asked you? Also, what is my name?"));


        // this is how we can send a message to a user, write to their file, and read from that file.
        // jaden.message()
        // jaden.writeToFile("Test");
        // jaden.readFromFile();
    }
}
