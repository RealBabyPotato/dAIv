import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import javax.naming.NameNotFoundException;
// import com.sun.net.httpserver.HttpServer;
// import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.net.URLDecoder;

public class SMSHandler implements HttpHandler {
// This overrides the Java HTTPHandler to Listen for an HTTP Request and produce a different response

@Override 
public void handle(HttpExchange t) throws IOException { 

  System.out.println("I got a message!");
  InputStream stream = t.getRequestBody();
   String str_full = new BufferedReader(
     new InputStreamReader(stream, "UTF-8"))
     .lines()
     .collect(Collectors.joining("\n"));
    System.out.println(str_full);  
 // extract the Body of the HTTPRequest from Twilio to String
   int str_start = str_full.indexOf("&Body")+6;
   String str = str_full.substring(str_start);
   int str_end = str.indexOf("&");
   String incoming_message = URLDecoder.decode(str.substring(0,str_end), "UTF-8"); 
   //System.out.println(incoming_message);
 // extract the incoming phone# of the HTTPRequest from Twilio to String
   str_start = str_full.indexOf("&From=%2B")+10;
   str = str_full.substring(str_start);
   str_end = str.indexOf("&");
   String incoming_phone = str.substring(0,str_end);


// create a Twilio xml reponse string that echos the incoming text
System.out.println(incoming_phone);

//testing

/*User dave = new User(new PhoneNumber(incoming_phone), "Buddy");

        // here is how we can use both twilio and chatgpt to send a message from chatgpt through twilio through our user! very cool!
        //jaden.message(GPTAPI.sendAndReceive(jaden, "How much wood would a woodchuck chuck if a woodchuck could chuck wood?").replaceAll("\\\\n", "\n")); // the replaceAll here just makes the \n function properly
dave.message(GPTAPI.sendAndReceive(dave, incoming_message));*/
    boolean tempFlag = false;

    for(User u : Main.RegisteredUsers){
        if(u.getPhoneNumber().toString().equals(incoming_phone)){
            u.message(GPTAPI.sendAndReceive(u, incoming_message));
            tempFlag = true;
            break;
        }
    }

    if(!tempFlag){
      TwilioSendMessageExample.messageUser(new User(new PhoneNumber(incoming_phone), "Unknown"), "Unknown user; you aren't registered!");
      // TwilioSendMessageExample.messageUser(new User(new PhoneNumber(incoming_phone)), "nknown"), "Unknown user; you aren't in RegisteredUsers!");
    }

//System.out.print(TwilioServer.send("2506613358",incoming_message));

String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Message>" + getReply(incoming_message) + "</Message></Response>";
//System.out.println(response);



t.sendResponseHeaders(200, response.length());
OutputStream os = t.getResponseBody();
os.write(response.getBytes());
os.close();

} 
  
public String getReply(String msg) {
      return "You just said:" + msg;
} 
  

 }
