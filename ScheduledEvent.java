import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.time.*;

interface Task{
	void execute();
}

public class ScheduledEvent{
	
	private Timer timeTracker;
	private TimerTask task;
	private String request;
	private Calendar date;
	private String field;
	
	//for one time tasks
	public ScheduledEvent(String r, Calendar d) {
		
		request = r;
		date = d;
		
		scheduleEvent(r, d);
	}

	//for repeated events
	public ScheduledEvent(String r, Calendar d, String f) {
		/*

		 */
		request = r;
		date = d;
		field = f;
		
		scheduleRepeatedEvent(r, d, f);
	}
	//for one time tasks
	public void scheduleEvent(String r, Calendar d) {
		task = new TimerTask() {
			public void run() {
				//do the thing
				//this is a test
				System.out.println("aaaaaa");
			}
		};
		
		timeTracker = new Timer();
		timeTracker.schedule(task, getTimeDiff(date));
	}
	//for repeated events
	// the parameter f is the amount by which the event is repeating
	public void scheduleRepeatedEvent(String r, Calendar d, String f) {
		
		task = new TimerTask() {
			public void run() {
				//do the thing
				//this is a test
				System.out.println("aaaaaa");
				if(f.equals("year")) {
					d.set(Calendar.YEAR, d.get(Calendar.YEAR)+1);
				}else if(f.equals("month")) {
					d.set(Calendar.MONTH, d.get(Calendar.MONTH)+1);
				}else if(f.equals("week")) {
					d.set(Calendar.WEEK_OF_YEAR, d.get(Calendar.WEEK_OF_YEAR)+1);
				}else if(f.equals("day")) {
					d.set(Calendar.DAY_OF_YEAR, d.get(Calendar.DAY_OF_YEAR)+1);
				}else if(f.equals("hour")) {
					d.set(Calendar.HOUR_OF_DAY, d.get(Calendar.HOUR_OF_DAY));
				}
				scheduleRepeatedEvent(r, d, f);
			}
		};
		
		timeTracker = new Timer();
		timeTracker.schedule(task, getTimeDiff(date));
	}

	// temporary constructor for immediate-start scheduled events
	public ScheduledEvent(long periodToRepeat, final Task runnableTask){
		this.task = new TimerTask() {
			@Override
			public void run() {
				runnableTask.execute();
			}
		};

		timeTracker = new Timer();
		timeTracker.scheduleAtFixedRate(this.task, 0, periodToRepeat);
		timeTracker.schedule(task, getTimeDiff(date), periodToRepeat);
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
