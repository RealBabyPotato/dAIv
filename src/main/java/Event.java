import java.util.*;

import java.text.DateFormat;

public class Event {
    protected static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale("en", "CA"));
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

        System.out.println(expiry.getTime() - new Date().getTime());
    }
}