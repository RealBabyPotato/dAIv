import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.twilio.type.PhoneNumber;

import java.net.URLDecoder;

public class SMSHandler implements HttpHandler {
    // This overrides the Java HTTPHandler to Listen for an HTTP Request and produce a different response
    @Override
    public void handle(HttpExchange t) throws IOException {

      System.out.println("I got a message!");

      InputStream stream = t.getRequestBody();
      String str_full = new BufferedReader(new InputStreamReader(stream, "UTF-8"))
         .lines()
         .collect(Collectors.joining("\n"));

      // System.out.println(str_full);
      // extract the Body of the HTTPRequest from Twilio to String

      int str_start = str_full.indexOf("&Body")+6;
      String str = str_full.substring(str_start);
      int str_end = str.indexOf("&");
      String incoming_message = URLDecoder.decode(str.substring(0,str_end), "UTF-8");

      String incomingMessageCopy = incoming_message;

      // if we receieve a help command that requires arguments, change the message so that our switch statement recognizes it -- then we can use incomingMessageCopy to extract the arguments
      if(incoming_message.length() >= 15 && incoming_message.substring(0, 15).equals("!changeusername")) { incoming_message = "!!changeusername"; }
      else if(incoming_message.length() >= 7 && incoming_message.substring(0, 7).equals("!report")) { incoming_message = "!!report"; }
      else if(incoming_message.startsWith("!help")) { incoming_message = "!help"; }
      else if(incoming_message.startsWith("!")) { incoming_message = "!!"; }

      // extract the incoming phone# of the HTTPRequest from Twilio to String
      str_start = str_full.indexOf("&From=%2B")+10;
      str = str_full.substring(str_start);
      str_end = str.indexOf("&");
      String incoming_phone = str.substring(0,str_end);

      User incoming_user = User.getUserWithNumberString(incoming_phone); // gets the user object of this incoming user (null if user does not exist)
      PhoneNumber numOfUser = new PhoneNumber(incoming_phone);

      //boolean userExists = false;

      switch(incoming_message){
        case "!help":
          TwilioSendMessageExample.messageNumber(numOfUser, "You are at the help menu. Please note that the commands *are* case sensitive. Commands:\n\n!help - brings you here!\n\n!changeusername {New Name} - changes your name in the eyes of dAIv.\n\n!report {issue} - reports an issue to Jaden.");
          break;

        case "!!report":
          try{
            TwilioSendMessageExample.messageNumber(new PhoneNumber("2508809769"), "dAIv REPORT from " + incoming_phone + ": " + incomingMessageCopy.substring(7));
            TwilioSendMessageExample.messageNumber(numOfUser, "Thanks for your report! You are actively making dAIv a better tool :)");
          } catch (Exception e){
            TwilioSendMessageExample.messageNumber(numOfUser, "Unfortunately something went wrong with your report. Please try again later.");
          }
          break;
        
          case "!!":
            TwilioSendMessageExample.messageNumber(numOfUser, "Unrecognized command. If you would like to see a list of commands, please use !help.");
            break;

        default:
          System.out.println("default");

          if(incoming_user != null){
            if(incoming_user.getIsInSetup()){ // if this user is setting up their account, go to the setup manager
              System.out.println("setting up in phase: " + incoming_user.getSetupPhase());
              SetupManager.setup(incoming_user, incoming_user.getSetupPhase(), incoming_message);
            }
            else {
              // this is the user that just messaged us, and they are registered.
              incoming_user.message(GPTAPI.sendAndReceive(incoming_user, incoming_message));
            }
          } 
          else {
              User new_user = User.registerUser(new PhoneNumber(incoming_phone));
              SetupManager.setup(new_user, 0, incoming_message);
          }

          /*for(User u : Main.RegisteredUsers){ // delegate this to a hashmap later
    
            if(u.getPhoneNumber().toString().equals(incoming_phone)){ // is this user's phone number the same as the incoming one?
    
              if(u.getIsInSetup()){ // if this user is setting up their account, go to the setup manager
                System.out.println("here in phase: " + u.getSetupPhase());
                SetupManager.setup(u, u.getSetupPhase(), incoming_message);
                userExists = true;
              }
              else {
                // this is the user that just messaged us, and they are registered.
                u.message(GPTAPI.sendAndReceive(u, incoming_message));
                userExists = true;
              }
    
              break;
            }
          }*/

          /*if(!userExists){
            User new_user = User.registerUser(new PhoneNumber(incoming_phone));
            SetupManager.setup(new_user, 0, incoming_message);
          }*/

          break;
      }

        
        // send response code back to twilio

        String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Message>" + getReply(incoming_message) + "</Message></Response>";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public String getReply(String msg) {
          return "You just said:" + msg;
    }
 }
