import com.twilio.type.PhoneNumber;

import java.util.ArrayList;

public class Main {

  public static ArrayList<User> RegisteredUsers = new ArrayList<User>();
  public static void main(String[] args) throws Exception {
    
    // Create a TwilioSMS instance to begin an HTTP service and Authenticate Client
    
    User.PopulateUsers(); // adds all users into memory - our 'registeredUsers' static list

    //User j = new User(new PhoneNumber("2508809769"), "Jaden");
    //backup.updateAndSaveUser(j);

    User new_user = User.registerUser(new PhoneNumber("2508809769"));
    SetupManager.setup(new_user, 0, "test startup message");

    System.out.println(new_user.getUserName());

    new_user.setUsername("jad");

    System.out.println(new_user.getUserName());
    
    // System.out.println(GPTAPI.sendAndReceive(j, "test"));

    //System.out.println(TwilioServer.send("+12506613358","HTTP Service is Running!"));

    /*for(User u : RegisteredUsers){
        System.out.println(u.getUserName());
    }*/

    //TwilioServer service = new TwilioServer();
  }

}