import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.twilio.type.PhoneNumber;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;

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
      else if(incoming_message.length() >= 10 && incoming_message.substring(0, 10).equals("!reminders")) { incoming_message = "!!reminders"; }// !reminders
      else if(incoming_message.length() >= 15 && incoming_message.substring(0, 15).equals("!removereminder")) { incoming_message = "!!removereminder"; }
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
          TwilioSendMessageExample.messageNumber(numOfUser, "You are at the help menu. Please note that the commands *are* case sensitive. Commands:\n\n!help - brings you here!\n\n!changeusername {New Name} - changes your name in the eyes of dAIv.\n\n!reminders - shows you your list of active reminders.\n\n!removereminder {Reminder Number} - deletes the specified reminder.\n\n!report {issue} - reports an issue to Jaden.");
          break;

        case "!!report":
          try{
            TwilioSendMessageExample.messageNumber(new PhoneNumber("2508809769"), "dAIv REPORT from " + incoming_phone + ": " + incomingMessageCopy.substring(7));
            TwilioSendMessageExample.messageNumber(numOfUser, "Thanks for your report! You are actively making dAIv a better tool :)");
            try {
              Calendar calendar = Calendar.getInstance();
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              String formattedDate = sdf.format(calendar.getTime());

              Files.write(Paths.get("reports.txt"), String.format("%s | %s | %s\n", formattedDate, incoming_phone, incomingMessageCopy.substring(7)).getBytes(), StandardOpenOption.APPEND); // formatted like date, number, report
              } catch (Exception e){ // catches an issue with logging the report
                System.out.println("issue logging report!");
              }
            } 
            
            catch (Exception e){ // catches an issue with sending the message
            TwilioSendMessageExample.messageNumber(numOfUser, "Unfortunately something went wrong with your report. Please try again later.");
          }
          break;

        case "!!changeusername": // an example incoming request could be: !changeusername Bob
          try{
            incoming_user.setUsername(incomingMessageCopy.substring(16));
            TwilioSendMessageExample.messageUser(incoming_user, "Your new name is: " + incomingMessageCopy.substring(16));
          } catch (Exception e){
            TwilioSendMessageExample.messageNumber(numOfUser, "Something went wrong when setting your username. Either you currently do not have an account, or you have input an invalid username. Please ensure your username only consists of normal characters.");
          }
          break;

        case "!!reminders":
          if(incoming_user.events.size() == 0){
            incoming_user.message("You have no active reminders.");
          } else{
            String msg = "You have " + incoming_user.events.size() + " active reminders/events.";
            for(int i = 0; i < incoming_user.events.size(); i++){
              if(incoming_user.events.get(i) instanceof Reminder){ 
                if(incoming_user.events.get(i).repeatInterval > 0){
                  msg += "\n\n{" + (i + 1) + "} | [Activates: " + Event.formattedDateFromUnix(incoming_user.events.get(i).expiryTime) + "] - " + ((Reminder)incoming_user.events.get(i)).remind + " (Repeats every " + ((Reminder)incoming_user.events.get(i)).repeatInterval / 60 + " minutes)"; // if this event repeats, tell the user when they use !reminders
                } else{
                  msg += "\n\n{" + (i + 1) + "} | [Activates: " + Event.formattedDateFromUnix(incoming_user.events.get(i).expiryTime) + "] - " + ((Reminder)incoming_user.events.get(i)).remind;
                }
                
              }
            }
            incoming_user.message(msg);
          }
          break;

        case "!!removereminder":
          try{
            int removeIndex = Integer.parseInt(incomingMessageCopy.substring(16));
            incoming_user.message("Removed reminder: " + ((Reminder)incoming_user.events.get(removeIndex - 1)).remind);
            incoming_user.events.remove(removeIndex - 1);
            backup.updateAndSaveUser(incoming_user);
          } catch(Exception e){
            incoming_user.message("There was an error removing the reminder. Please try again. (Example: !removereminder 1)");
          }
          break;
        
        case "!!":
          TwilioSendMessageExample.messageNumber(numOfUser, "Unrecognized command. If you would like to see a list of commands, please use !help.");
          break;

        default:
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
