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

      System.out.println(str_full);
      // extract the Body of the HTTPRequest from Twilio to String

      int str_start = str_full.indexOf("&Body")+6;
      String str = str_full.substring(str_start);
      int str_end = str.indexOf("&");
      String incoming_message = URLDecoder.decode(str.substring(0,str_end), "UTF-8");

      // extract the incoming phone# of the HTTPRequest from Twilio to String
      str_start = str_full.indexOf("&From=%2B")+10;
      str = str_full.substring(str_start);
      str_end = str.indexOf("&");
      String incoming_phone = str.substring(0,str_end);

       // create a Twilio xml reponse string that echos the incoming text
       System.out.println(incoming_phone);

       boolean userExists = false;

        for(User u : Main.RegisteredUsers){

          System.out.println("Incoming: " + incoming_phone + " | Main: " + u.getPhoneNumberAsString());

          if(u.getPhoneNumber().toString().equals(incoming_phone)){ // is this user's phone number the same as the incoming one?

            if(u.getIsInSetup()){ // if this user is setting up their account, go to the setup manager
              System.out.println("match! in phase: " + u.getSetupPhase());
              SetupManager.setup(u, u.getSetupPhase(), incoming_message);
            }
            else {
              // this is the user that just messaged us, and they are registered.
              u.message(GPTAPI.sendAndReceive(u, incoming_message));
              userExists = true;
            }

            break;
          }
        }

      if(!userExists){
        User new_user = User.registerUser(new PhoneNumber(incoming_phone), incoming_message);
        SetupManager.setup(new_user, 0, incoming_message);
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
