import com.google.gson.annotations.Expose;

import java.util.*;
import java.util.concurrent.TimeUnit;
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

    public static String formattedDateFromUnix(long unix){
        Date date = new Date();
        date.setTime(unix);

        return dateFormat.format(date) + " " + timeFormat.format(date);
    }

    public Event(User user, long expiry){
        this.startTime = new Date().getTime();
        this.expiryTime = expiry;
        this.owner = user;
    }

    public Event(long expiry){
        this.expiryTime = expiry;
        // System.out.println("instantiating event with null constructor -- you did something wrong...");
    }

    public static long currentTimeSeconds(){
        long milli = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(milli);
    }
}

class Reminder extends Event{
    @Expose
    String remind;
    public Reminder(User user, long expiry, String remind){
        super(user, expiry);
        this.remind = remind;
        System.out.println("Time until reminder ends: " + (expiry - currentTimeSeconds()));

        expiryTimer = new Timer();
        expiryTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                user.message(GPTAPI.sendAndReceive(user, "SYSTEM: on " + dateFormat.format(startTime) + " asked to be reminded about something. this is what they would like to be reminded about - remind them, and tell them when they asked to be reminded about it. reminder: " + remind));
                System.out.println("Sending reminder of event: " + remind);

                owner.events.remove(this);
                backup.updateAndSaveUser(owner);
            }
        }, expiry - currentTimeSeconds());

        user.addEvent(this);
        float hoursUntilProc = (expiry - currentTimeSeconds()) / 60 ;
        user.message("Successfully set reminder to end in " + Math.round(hoursUntilProc) + " minute(s)!");
        backup.updateAndSaveUser(user);
    }



    public Reminder(long beginTime, long expiryTime, String remind){ // THIS CONSTRUCTOR IS ONLY FOR WHEN WE ARE CREATING A REMINDER FROM THE BACKUP!
        super(expiryTime);
        this.startTime = beginTime;
        this.remind = remind;
        // System.out.println(a);
    }

    public void begin(){
        // begin timer. this should only be called when we are creating a reminder again via gson
        if(owner != null){
            expiryTimer = new Timer();
            try{
                expiryTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        owner.message(GPTAPI.sendAndReceive(owner, "SYSTEM: on " + dateFormat.format(startTime) + " asked to be reminded about something. this is what they would like to be reminded about - remind them, and tell them when they asked to be reminded about it. reminder: " + remind));
                        System.out.println("Sending reminder of event: " + remind);
                        owner.events.remove(this);
                        backup.updateAndSaveUser(owner);
                    }
                }, this.expiryTime - currentTimeSeconds());

                System.out.println("Time until reminder ends: " + (this.expiryTime - currentTimeSeconds()));

            } catch (IllegalArgumentException e){
                System.out.println("Hi! Unfortunately it looks like a reminder that you set on " + Event.formattedDateFromUnix(this.startTime) + " elapsed while our servers were down. It instructed you to '" + this.remind + "' on " + Event.formattedDateFromUnix(this.expiryTime));
                owner.events.remove(this);
                backup.updateAndSaveUser(owner);
            }
            return;
        }

        System.out.println("ERROR: attempting to begin a Reminder with no owner!");
    }
}