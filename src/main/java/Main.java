import com.twilio.type.PhoneNumber;

import java.util.ArrayList;

public class Main {

  public static ArrayList<User> RegisteredUsers = new ArrayList<User>();
  public static void main(String[] args) throws Exception {
    
    // Create a TwilioSMS instance to begin an HTTP service and Authenticate Client
    
    //System.out.println(TwilioServer.send("+12506613358","HTTP Service is Running!"));
    User.PopulateUsers();
    //User j = new User(new PhoneNumber("2508809769"), "Jaden");

    for(User u : RegisteredUsers){
        System.out.println(u.getPhoneNumber());
    }

    TwilioServer service = new TwilioServer();

    // Send a test Message
    System.out.println(service);

    
  }

}