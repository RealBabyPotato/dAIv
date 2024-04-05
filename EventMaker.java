import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;

public class EventMaker {

	public static void makeEvent(String r, Calendar d) {
		if(isValid(d)) {
			ScheduledEvent a = new ScheduledEvent(r, d);
		}
	}
	
	public static void makeEvent(String r, Calendar d, long rep) {
		if(isValid(d)) {
			ScheduledEvent a = new ScheduledEvent(r, d, rep);
		}
	}
	
	public static boolean isValid(Calendar c) {
		if(getTimeDiff(c) < 0) {
			return false;
		}
		return true;
	}
	
	public static long getTimeDiff(Calendar cal) {
		Clock clocka = Clock.system(ZoneId.of("UTC"));
		Clock clock = Clock.offset(clocka, Duration.ofHours(-7));
		
		Instant start = Instant.now(clock);
		
		String s = cal.toInstant().toString();
		CharSequence cs = s;
		Instant end = Instant.parse(cs);
		
		
		return Duration.between(start, end).toMillis();
		
	}	
	
}
