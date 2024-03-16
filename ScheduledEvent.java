import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.time.*;


public class ScheduledEvent{
	
	private Timer timeTracker;
	private TimerTask task;
	private String response;
	
	public ScheduledEvent(String r, Calendar d) {

		response = r;

		task = new TimerTask() {
			public void run() {
				//do the thing
				//this is a test
				System.out.println("aaaaaa");
			}
		};
		
		//get timeToSend by asking for milliseconds from chat
		timeTracker = new Timer();
		timeTracker.schedule(task, getTimeDiff(d);
		
	}

	//for repeated events
	//periodToRepeat is the time in which the task should be repeated, such as 24 hours. in milliseconds.
	public ScheduledEvent(String r, Calendar d, long periodToRepeat) {
		
		response = r;
		
		task = new TimerTask() {
			public void run() {
				//do the thing
				//this is a test
				System.out.println("aaaaaa");
			}
		};
		
		//get timeToSend by asking for milliseconds from chat
		timeTracker = new Timer();
		timeTracker.schedule(task, getTimeDiff(d), periodToRepeat);	
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
