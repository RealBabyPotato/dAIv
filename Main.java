import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.type.PhoneNumber;
import org.json.simple.parser.ParseException;
import com.twilio.rest.api.v2010.account.Message;

import java.util.ArrayList;

// test for harry cai
class Main{

    Message mostRecentMessage = null;
    public static ArrayList<User> RegisteredUsers = new ArrayList<User>(); // not sure if we should keep this...

    public static void main(String[] args){
        Twilio.init(TwilioSendMessageExample.ACCOUNT_SID, TwilioSendMessageExample.AUTH_TOKEN);
        System.out.println("Hello my name is DAIV please interact with me");

        // this is how we register a new user atm
        User jaden = new User(new PhoneNumber("2508809769"));
        User ethan = new User(new PhoneNumber("7785334028"));

        JSONManager jsonHandler = new JSONManager(jaden);
        
        TwilioSendMessageExample.messageUser(ethan, "ethan is lame and cool and blah blah blah");
        
        

        // this is how we send a message to a user. this method will likely be moved out of the TwilioSendMessageExample class soon.
        //TwilioSendMessageExample.messageUser(jaden, "This is an example of a text to the User object Jaden.");
        // this is also how we can send a message to a user
        //jaden.message("asdf");

        main.replyAlways("Replying!");

    }

    //region super janky
    // !!! this is a *temporary* workaround to not having access to webhooks and the server while will/mr. h are away! this is a very bad implementation of what it's trying to do. it is for debug purposes only.
    private void replyAlways(String reply){
        // if this receives two messages within 5 seconds it will not reply to the first one it receives -- seriously, do not use this!! - jaden
        ScheduledEvent t = new ScheduledEvent(5000, new Task(){
            @Override
            public void execute(){
                if(refreshMostRecentMessage()){ // if there has been a new message:
                    // System.out.println(mostRecentMessage.getFrom());
                    try{
                        findUser(mostRecentMessage.getFrom()).message("Reply");
                    } catch(NullPointerException e){
                        System.out.println("Failed to find reply user phone number");
                    }
                }
                System.out.println("run");
            }
        });
    }

    // refreshes the most recent message and tells us if it has changed.
    private boolean refreshMostRecentMessage(){
        ResourceSet<Message> messages = Message.reader().limit(1).read();

        for(Message record : messages) {
            if(!record.equals(mostRecentMessage)){
                this.mostRecentMessage = record;
                System.out.println("Updating MRM");
                return true;
            }
            return false;
        }
        return false;
    }

    private User findUser(PhoneNumber num){
        for(User user : RegisteredUsers){
            System.out.println(user.phoneNumber);
            if(user.phoneNumber.equals(num)){
                return user;
            }
        }

        return null;
    }
    //endregion
}
