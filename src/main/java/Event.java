import com.google.gson.annotations.Expose;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Event {
    public static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale("en", "CA"));
    public static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, new Locale("en", "CA"));
    
    public static SimpleDateFormat datePattern = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

    @Expose
    protected long startTime;
    @Expose
    protected long expiryTime;
    @Expose
    protected long repeatInterval = -1;

    protected User owner;
    protected Timer expiryTimer;

    public static String formattedDateFromUnix(long unix){
        unix *= 1000; //date takes time in millis not seconds
        Date date = new Date();
        date.setTime(unix);

        return dateFormat.format(date) + " " + timeFormat.format(date);
    }

    public Event(User user, long expiry){
        this.startTime = currentTimeSeconds();
        this.expiryTime = expiry;
        this.owner = user;
    }

    public Event(long expiry){
        this.expiryTime = expiry;
        // System.out.println("instantiating event with null constructor -- you did something wrong...");
    }

    public static long currentTimeSeconds(){
        long milli = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(milli) - 25200; // PST time zone is -7 * 60 * 60
    }

    public static long dateToSecondsFromEpoch(String date){ // should be formatted like 15 Jul 2024 22:25:40
        Date day;
        try {
            day = datePattern.parse(date);
        } catch (ParseException e) {
            return 1; // if whatever we are given is not in the correct format, return 1; this will be recognized as an invalid end time
        }

        System.out.println(currentTimeSeconds());

        //System.out.println(day.getTime() / 1000) + 25200 - currentTimeSeconds());

        return (day.getTime() / 1000); //+ 25200; // PST diff
    }

    public static void main(String[] args){
        System.out.println(dateToSecondsFromEpoch("6 Jul 2024 00:49:00"));
    }
}

class Reminder extends Event{
    @Expose
    String remind;
    public Reminder(User user, long expiry, String remind, long repeatInterval){
        super(user, expiry);
        this.remind = remind;
        this.repeatInterval = repeatInterval;
        System.out.println("Time until reminder ends: " + (expiry - currentTimeSeconds()));

        Reminder instance = this;
        expiryTimer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                owner.message(GPTAPI.sendAndReceive(owner, "SYSTEM: on " + formattedDateFromUnix(startTime) + " asked to be reminded about something. this is what they would like to be reminded about - remind them, and tell them when they asked to be reminded about it (avoid starting your sentence as a reply to this prompt). reminder: " + remind));
                System.out.println("Sending reminder of event: " + remind);

                if(repeatInterval < 0){ // if this reminder does not repeat, remove it
                    owner.events.remove(instance);
                    backup.updateAndSaveUser(owner);
                } else {
                    instance.expiryTime += repeatInterval;
                    backup.updateAndSaveUser(owner);
                }
            }
        };

        if(repeatInterval > 0){ // if this repeat interval has been changed (i.e. repeat != -1)
            expiryTimer.scheduleAtFixedRate(task, repeatInterval * 1000, repeatInterval * 1000);
            user.message("Successfully set reminder to repeat every " + repeatInterval / 60 + " minutes! You can view all of your active reminders with !reminders.");
        } else{
            expiryTimer.schedule(task, (expiry - currentTimeSeconds())*1000);
            user.message("Successfully set reminder to end " + formattedDateFromUnix(expiry) + "! You can view all of your active reminders with !reminders.");
        }
        

        user.addEvent(this);
        backup.updateAndSaveUser(user);
    }



    public Reminder(long beginTime, long expiryTime, String remind, long repeatTime){ // THIS CONSTRUCTOR IS ONLY FOR WHEN WE ARE CREATING A REMINDER FROM THE BACKUP!
        super(expiryTime);
        this.startTime = beginTime;
        this.remind = remind;
        this.repeatInterval = repeatTime;
        // System.out.println(a);
    }

    public void begin(){
        // begin timer. this should only be called when we are creating a reminder again via gson
        System.out.println(this.repeatInterval);
        if(owner != null){
            Reminder instance = this;
            expiryTimer = new Timer();
            try{
                expiryTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        owner.message(GPTAPI.sendAndReceive(owner, "SYSTEM: on " + formattedDateFromUnix(startTime) + " asked to be reminded about something. this is what they would like to be reminded about - remind them, and tell them when they asked to be reminded about it (avoid starting your sentence as a reply to this prompt). reminder: " + remind));
                        System.out.println("Sending reminder of event: " + remind);

                        owner.events.remove(instance);
                        backup.updateAndSaveUser(owner);
                   }
                }, (this.expiryTime - currentTimeSeconds())*1000);

                System.out.println("Time until reminder ends: " + (this.expiryTime - currentTimeSeconds()));

            } catch (IllegalArgumentException e){
                owner.message("Hi! Unfortunately it looks like a reminder that you set on " + Event.formattedDateFromUnix(this.startTime) + " elapsed while our servers were down. It instructed you to '" + this.remind + "' on " + Event.formattedDateFromUnix(this.expiryTime));
                owner.events.remove(this);
                backup.updateAndSaveUser(owner);
            }
            return;
        }

        System.out.println("ERROR: attempting to begin a Reminder with no owner!");
    }
}