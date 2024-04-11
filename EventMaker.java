import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;

public class EventMaker {

	// Takes a string r and a calendar d for request and date and time
	// and turns them into an event that will run at the date and time specified by the calendar
	// d must be set in the future
	public static void makeEvent(String r, Calendar d) {
		if (isValid(d)) {
			ScheduledEvent a = new ScheduledEvent(r, d);
		}
	}

	// Takes a string r for request and calendar d for a date and time
	// Makes an event that runs at date and time of calendar and then repeats in a unit of time, specified by String f
	// String f takes "year", "month", "week", "day", "hour", or "minute" as input
	// d must be set in the future
	public static void makeEvent(String r, Calendar d, String f) {
		if (isValid(d)) {
			ScheduledEvent a = new ScheduledEvent(r, d, f);
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
