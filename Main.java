import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.simple.parser.ParseException;

class Main{
    public static void main(String[] args) throws ParseException {
        Twilio.init(TwilioSendMessageExample.ACCOUNT_SID, TwilioSendMessageExample.AUTH_TOKEN);
        System.out.println("Hello my name is DAIV please interact with me");

        // this is how we register a new user atm
        User jaden = new User(new PhoneNumber("2508809769"), "Jaden");
        User ethan = new User(new PhoneNumber("7785334028"), "Ethan");

        JSONManager jadenConversation = new JSONManager(jaden.userName);
        jadenConversation.addConversation("test test", "reply reply");
        jadenConversation.read_File(jaden.userName);

        
        //TwilioSendMessageExample.messageUser(ethan, "ethan is lame and cool and blah blah blah");
        
        // this is how we send a message to a user. this method will likely be moved out of the TwilioSendMessageExample class soon.
        TwilioSendMessageExample.messageUser(jaden, "This is an example of a text to the User object Jaden.");
    }
}
