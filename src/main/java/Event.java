import java.util.*;

import java.text.DateFormat;

public class Event {
    public static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale("en", "CA"));
    public static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, new Locale("en", "CA"));
    protected Date startTime;
    protected Date expiryTime;
    protected User owner;
    protected Timer expiryTimer;

    public Event(User user, Date expiry){
        this.startTime = new Date();
        this.expiryTime = expiry;
        this.owner = user;
    }
}

class Reminder extends Event{
    public Reminder(User user, Date expiry, String remind){
        super(user, expiry);

        expiryTimer = new Timer();
        expiryTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                user.message(GPTAPI.sendAndReceive(user, "SYSTEM: on " + dateFormat.format(startTime) + " asked to be reminded about something. this is what they would like to be reminded about - remind them, and tell them when they asked to be reminded about it. reminder: " + remind));
                System.out.println("Sending reminder of event: " + remind);
            }
        }, expiry.getTime() - new Date().getTime());

        System.out.println("Time until reminder ends: " + (expiry.getTime() - new Date().getTime()));
        user.addEvent(this);
        float hoursUntilProc = (expiry.getTime() - new Date().getTime()) / 1000 / 60 / 60;
        user.message("Successfully set reminder to end in " + Math.round(hoursUntilProc) + " hours!");
    }
}