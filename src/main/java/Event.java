import com.google.gson.annotations.Expose;

import java.util.*;

import java.text.DateFormat;

public class Event {
    public static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale("en", "CA"));
    public static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, new Locale("en", "CA"));
    @Expose
    protected long startTime;
    @Expose
    protected long expiryTime;
    protected User owner;
    protected Timer expiryTimer;

    public Event(User user, long expiry){
        this.startTime = new Date().getTime();
        this.expiryTime = expiry;
        this.owner = user;
    }

    public Event(long expiry){
        System.out.println("instantiating event with null constructor -- you did something wrong...");
    }
}

class Reminder extends Event{
    @Expose
    String remind;
    public Reminder(User user, long expiry, String remind){
        super(user, expiry);
        this.remind = remind;

        expiryTimer = new Timer();
        expiryTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                user.message(GPTAPI.sendAndReceive(user, "SYSTEM: on " + dateFormat.format(startTime) + " asked to be reminded about something. this is what they would like to be reminded about - remind them, and tell them when they asked to be reminded about it. reminder: " + remind));
                System.out.println("Sending reminder of event: " + remind);
            }
        }, expiry - new Date().getTime());

        System.out.println("Time until reminder ends: " + (expiry - new Date().getTime()));
        user.addEvent(this);
        float hoursUntilProc = (expiry - new Date().getTime()) / 1000 / 60 ;
        user.message("Successfully set reminder to end in " + Math.round(hoursUntilProc) + " minute(s)!");
        backup.updateAndSaveUser(user);
    }

    public Reminder(long beginTime, long expiryTime, String remind){
        super(expiryTime);
        // System.out.println(a);
    }

    public void begin(){
        // begin timer. this should only be called when we are creating a reminder again via gson
        if(owner != null){

            return;
        }

        System.out.println("ERROR: attempting to begin a Reminder with no owner!");
    }
}