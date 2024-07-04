import com.twilio.type.PhoneNumber;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

// TODO: !removereminder and repeated events

public class Main {

  public static ArrayList<User> RegisteredUsers = new ArrayList<User>();
  public static void main(String[] args) throws Exception {
    
    Event.timeFormat.setTimeZone(TimeZone.getTimeZone("PST"));
    // Create a TwilioSMS instance to begin an HTTP service and Authenticate Client

    // this effectively loads everything from our backup into memory. (including events!!)
    User.PopulateUsers(); // adds all users into memory - our 'registeredUsers' static list

    //System.out.println(Event.timeFormat.format(new Date()) + " " + Event.dateFormat.format(new Date()));
    //System.out.println(Event.formattedDateFromUnix(Event.currentTimeSeconds()));

    //GPTAPI.sendAndReceive(RegisteredUsers.get(0), "Remind me to make bread today at 10PM.");

    TwilioServer service = new TwilioServer();
  }
}