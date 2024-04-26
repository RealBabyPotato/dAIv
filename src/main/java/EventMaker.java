import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;

public class EventMaker {

	// Takes a string m for message sent to chatgpt
	//n for the phone number
	//u for user's name
	//a calendar d for request and date and time
	// and turns them into an event that will run at the date and time specified by the calendar
	// d must be set in the future
	public static void makeEvent(String m, String n, String u, Calendar d) {
		if (isValid(d)) {
	//		ScheduledEvent a = new ScheduledEvent(m, n, u, d);
		}
	}
	
	// Takes a string m for message sent to chatgpt
	//n for the phone number
	//u for user's name
	//calendar d for a date and time
	// Makes an event that runs at date and time of calendar and then repeats in a unit of time, specified by String f
	// String f takes "year", "month", "week", "day", "hour", or "minute" as input
	// d must be set in the future
	//int a is the amount of time units
	public static void makeEvent(String m, String n, String u, Calendar d, String f, int a) {
		if (isValid(d)) {
			//ScheduledEvent b = new ScheduledEvent(m, n, u, d, f, a);
		}
	}

	// Checks if the event is set to run in the future and not the past
	public static boolean isValid(Calendar c) {
		return getTimeDiff(c) >= 0;
	}

	// Gets the time difference between the current time and a specified date
	public static long getTimeDiff(Calendar cal) {
		Clock clocka = Clock.system(ZoneId.of("UTC"));
		Clock clock = Clock.offset(clocka, Duration.ofHours(-7));
		
		Instant start = Instant.now(clock);
		
		String s = cal.toInstant().toString();
		CharSequence cs = s;
		Instant end = Instant.parse(cs);

		return Duration.between(start, end).toMillis();
	}	

	// Cancels an event
//	public static boolean cancelTask(ScheduledEvent s) {
//		return s.getObject().cancel();
//	}
}