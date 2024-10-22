import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;


class TwilioServer {

  private static final String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
	private static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";
	private static final String ACCOUNT_NUMBER = "+1YOUR_TWILIO_PHONE_NUMBER";

  public TwilioServer() throws Exception {
      // authenticate the Twilio Client
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

      // create and HTTP Service to listen for Twilio sms messages
      HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
      server.createContext("/", new SMSHandler());
      server.setExecutor(null); // creates a default executor
      server.start();
      System.out.println("Server up and running.");
    }

  
  public static String send(String phone, String msg) {   
    
    // method to Send an sms to any number 
    Message sms = Message.creator(new com.twilio.type.PhoneNumber(phone), new com.twilio.type.PhoneNumber(ACCOUNT_NUMBER), msg).create();

    return sms.getSid();
  }

  
}
 