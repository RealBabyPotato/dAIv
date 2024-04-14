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
	private int amount;
	
	//for one time tasks
	public ScheduledEvent(String r, Calendar d) {
		
		request = r;
		date = d;
		
		scheduleEvent(r, d);
	}

	//for repeated events
	public ScheduledEvent(String r, Calendar d, String f, int a) {
		/*

		 */
		request = r;
		date = d;
		field = f;
		amount = a;
		
		scheduleRepeatedEvent(r, d, f, a);
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
	// the parameter f is the time unit by which the event is repeating
	//a is the amount of time units (eg. 2 minutes, 3 hours, 5 days)
	public void scheduleRepeatedEvent(String r, Calendar d, String f, int a) {
		
		task = new TimerTask() {
			public void run() {
				//do the thing
				//this is a test
				System.out.println("aaaaaa");
				if(f.equals("year")) {
					d.set(Calendar.YEAR, d.get(Calendar.YEAR)+a);
				}else if(f.equals("month")) {
					d.set(Calendar.MONTH, d.get(Calendar.MONTH)+a);
				}else if(f.equals("week")) {
					d.set(Calendar.WEEK_OF_YEAR, d.get(Calendar.WEEK_OF_YEAR)+a);
				}else if(f.equals("day")) {
					d.set(Calendar.DAY_OF_YEAR, d.get(Calendar.DAY_OF_YEAR)+a);
				}else if(f.equals("hour")) {
					d.set(Calendar.HOUR_OF_DAY, d.get(Calendar.HOUR_OF_DAY)+a);
				}else if(f.equals("minute")) {
					d.set(Calendar.MINUTE, d.get(Calendar.MINUTE)+a);
				}else if(f.equals("second")){
					d.set(Calendar.SECOND, d.get(Calendar.SECOND)+a);
				}
				scheduleRepeatedEvent(r, d, f, a);
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
	
	public TimerTask getTask() {
		return task;
	}
	
	public String getRequest() {
		return request;
	}

	public String getTime(){ // Format as a YYYY-MM-DD HH:mm:ss string
		return String.format("%04d-%02d-%02d %02d:%02d:%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), date.get(Calendar.SECOND)); // Format as YYYY-MM-DD HH:mm:ss string
	}
	

	
	
}
