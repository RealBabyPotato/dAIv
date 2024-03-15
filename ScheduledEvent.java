import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.time.*;


public class ScheduledEvent{
	
	private Timer timeTracker;
	private TimerTask task;
	private long timeToSend;
	private String response;
	
	public ScheduledEvent(int id, String r, Calendar d) {

		response = r;
		
		timeToSend = getTimeDiff(d);
		
		task = new TimerTask() {
			public void run() {
				//do the thing
				//this is a test
				System.out.println("aaaaaa");
			}
		};
		
		//get timeToSend by asking for milliseconds from chat
		timeTracker = new Timer();
		timeTracker.schedule(task, timeToSend);
		
	}
	
	//input the date and time at which message should be sent
	//returns the number of milliseconds between the current time and the time it should be sent
	public long getTimeDiff(Calendar cal) {
		Clock clocka = Clock.system(ZoneId.of("UTC"));
		Clock clock = Clock.offset(clocka, Duration.ofHours(-7));
		
		Instant start = Instant.now(clock);
		
		String s = cal.toInstant().toString();
		CharSequence cs = s;
		Instant end = Instant.parse(cs);
		
		return Duration.between(start, end).toMillis();
		
	}	
	
}
