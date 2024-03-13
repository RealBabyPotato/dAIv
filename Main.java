import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

class Main{
    public static void main(String[] args){
        Twilio.init(TwilioSendMessageExample.ACCOUNT_SID, TwilioSendMessageExample.AUTH_TOKEN);
        System.out.println("Hello my name is DAIV please interact with me");

        // this is how we register a new user atm
        User jaden = new User(new PhoneNumber("2508809769"));

        // this is how we send a message to a user. this method will likely be moved out of the TwilioSendMessageExample class soon.
        TwilioSendMessageExample.messageUser(jaden, "This is an example of a text to the User object Jaden.");
    }
}
