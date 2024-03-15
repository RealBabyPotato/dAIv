import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import org.json.simple.parser.ParseException;

class Main{
    public static void main(String[] args) throws ParseException {
        Twilio.init(TwilioSendMessageExample.ACCOUNT_SID, TwilioSendMessageExample.AUTH_TOKEN);
        System.out.println("Hello my name is DAIV please interact with me");

        // this is how we register a new user atm
        User jaden = new User(new PhoneNumber("2508809769"), "Jaden");
        User bob = new User(new PhoneNumber("123123123123123"), "Bob");
        //User ethan = new User(new PhoneNumber("7785334028"), "Ethan");

        jaden.writeToFile("test1");
        jaden.writeToFile("test2");
        jaden.readFromFile();

        bob.readFromFile();

        // this is how we send a message to a user. this method will likely be moved out of the TwilioSendMessageExample class soon.
        //TwilioSendMessageExample.messageUser(jaden, "This is an example of a text to the User object Jaden.");
    }
}
