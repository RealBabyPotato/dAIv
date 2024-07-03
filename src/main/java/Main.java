import com.twilio.type.PhoneNumber;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main {

  public static ArrayList<User> RegisteredUsers = new ArrayList<User>();
  public static void main(String[] args) throws Exception {
    
    // Create a TwilioSMS instance to begin an HTTP service and Authenticate Client

    // this effectively loads everything from our backup into memory. (including events!!)
    User.PopulateUsers(); // adds all users into memory - our 'registeredUsers' static list

    //User jaden = new User(new PhoneNumber("2508809769"), "Jaden");
    // System.out.println(GPTAPI.sendAndReceive(jaden, "in 5 minutes remind me to wash my hands"));
    //backup.updateAndSaveUser(j);

    /*User new_user = User.registerUser(new PhoneNumber("2508809769")); // add user to main.user and backup
    SetupManager.setup(new_user, 0, "test startup message");

    System.out.println(new_user.getUserName());

    new_user.setUsername("jad");

    System.out.println(new_user.getUserName());

    SetupManager.setup(new_user, new_user.getSetupPhase(), "message");*/
    
    // System.out.println(GPTAPI.sendAndReceive(j, "test"));

    //System.out.println(TwilioServer.send("+12506613358","HTTP Service is Running!"));

    /*Date asdf = new Date();
    asdf.setTime(System.currentTimeMillis() + 10 * 1000);

    Reminder reminder = new Reminder(j, asdf, "get some bread at fairway's");*/

    TwilioServer service = new TwilioServer();
  }
}