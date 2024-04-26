import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Main_old {
    public static ArrayList<User> RegisteredUsers = new ArrayList<User>(); // not sure if we should keep this...

    public static void main(String[] args) throws NameNotFoundException, InterruptedException {
        Twilio.init(TwilioSendMessageExample.ACCOUNT_SID, TwilioSendMessageExample.AUTH_TOKEN);
        User.PopulateUsers();
        for(User user : RegisteredUsers){
            System.out.println(user.getUserName());
        }

        // this is how we register a new user atm
        //User jaden = new User(new PhoneNumber("2508809769"), "Jaden");
        //User ethan = new User(new PhoneNumber("7785334028"), "Ethan");

        // here is how we can use both twilio and chatgpt to send a message from chatgpt through twilio through our user! very cool!
        //jaden.message(GPTAPI.sendAndReceive(jaden, "How much wood would a woodchuck chuck if a woodchuck could chuck wood?").replaceAll("\\\\n", "\n")); // the replaceAll here just makes the \n function properly
        //jaden.message("guess who has written a script for the server and will now make a working console for it");
        // System.out.println(GPTAPI.sendAndReceive(ethan, "What was the last thing I asked you? Also, what is my name?"));
        // System.out.println(GPTAPI.sendAndReceive(jaden, "What was the last thing I asked you? Also, what is my name?"));


        // this is how we can send a message to a user, write to their file, and read from that file.
        // jaden.message()
        // jaden.writeToFile("Test");
        // jaden.readFromFile();

        Scanner kboard = new Scanner(System.in);
        final String INFO = "\033[1;34m";
        final String ERR = "\033[1;31m";
        final String CLR = "\033[0;0m";
        while (true){
            String line = kboard.nextLine().strip();
            String[] cmd = line.strip().split("\\s+");
            switch (cmd[0]){
                case "exit":
                    kboard.close();
                    System.exit(0);
                case "help":
                    System.out.println(INFO + "~dAIv help~\ncommands: " + CLR + "\nhelp: you already found this one\nexit: stop the dAIv server and exit this console\nuser: \nuser add $name $phoneNumber: create a new user object with phone number $phoneNumber and name $name\nuser remove $phoneNumber: remove the user with phone number $phoneNumber\nuser list: list all users with phone numbers and names\nsend $phoneNumber $message: Send $message to $phoneNumber");
                    break;
                case "send":
                    for (User user : RegisteredUsers){
                        if (user.getPhoneNumber().toString().equals(new PhoneNumber(cmd[1]).toString())){
                            user.message(line.split("\\s+", 3)[2]);
                        }
                    }
                    break;
                case "user":
                    switch (cmd[1]){
                        case "add":
                            // FIXME This adds users to the RegisteredUsers ArrayList - we may remove it
                            RegisteredUsers.add(new User(new PhoneNumber(cmd[3]), cmd[2]));
                            break;
                        case "remove":
                            for (int i = 0; i < RegisteredUsers.size(); i++){
                                if (RegisteredUsers.get(i).phoneNumber.equals(new PhoneNumber(cmd[2]))){
                                    RegisteredUsers.remove(i);
                                    i = RegisteredUsers.size();
                                }
                            }
                            break;
                        case "list":
                            for (User user : RegisteredUsers){
                                System.out.println(user.getPhoneNumber() + " " + user.getUserName());
                            }
                            break;
                        default:
                            System.out.println("Invalid command.");
                            break;
                    }
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;

            }
        }
    }
}
