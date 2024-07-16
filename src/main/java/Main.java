import java.util.ArrayList;

// TODO: add repeated events and maybe !refresh for resetting thread?

public class Main {

  public static ArrayList<User> RegisteredUsers = new ArrayList<User>();
  public static void main(String[] args) throws Exception {
    // Create a TwilioSMS instance to begin an HTTP service and Authenticate Client

    // this effectively loads everything from our backup into memory. (including events!!)
    try{

      User.PopulateUsers(); // adds all users into memory - our 'registeredUsers' static list
      TwilioServer service = new TwilioServer();

    } catch(java.util.ConcurrentModificationException e){
      System.out.println("Something went wrong, please stop and rerun the program in ~5 seconds.");
    }

  }
}