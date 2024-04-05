import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;

public class EventMaker {

	public static boolean makeEvent(Calendar d) {
		if (getTimeDiff(d) < 0) {
			return false;
		}
		ScheduledEvent a = new ScheduledEvent(d);
		return true;
	}
	
	public static boolean makeEvent(Calendar d, long rep) {
		if (getTimeDiff(d) < 0) {
			return false;
		}
		ScheduledEvent a = new ScheduledEvent(d, rep);
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
