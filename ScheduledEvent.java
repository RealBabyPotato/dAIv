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
	private String message;
	private Calendar date;
	private String field;
	private int amount;
	private String phonenum;
	private String user;
	
	//for one time tasks
	public ScheduledEvent(String m, String n, String u, Calendar d) {
		
		message = m;
		phonenum = n;
		user = u;
		date = d;
		
		scheduleEvent(message, phonenum, user, date);
	}

	//for repeated events
	public ScheduledEvent(String m, String n, String u, Calendar d, String f, int a) {
		message = m;
		phonenum = n;
		user = u;
		date = d;
		field = f;
		amount = a;
		
		scheduleRepeatedEvent(message, phonenum, user, date, field, amount);
	}
	
	//for one time tasks
	public void scheduleEvent(String m, String n, String u, Calendar d) {
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
	public void scheduleRepeatedEvent(String m, String n, String u, Calendar d, String f, int a) {
		
		task = new TimerTask() {
			public void run() {
				//do the thing	
				
				//this is a test
				System.out.println("aaaaaa");
				if(f.equals("year")) {
					date.set(Calendar.YEAR, date.get(Calendar.YEAR)+amount);
				}else if(f.equals("month")) {
					date.set(Calendar.MONTH, date.get(Calendar.MONTH)+amount);
				}else if(f.equals("week")) {
					date.set(Calendar.WEEK_OF_YEAR, date.get(Calendar.WEEK_OF_YEAR)+amount);
				}else if(f.equals("day")) {
					date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR)+amount);
				}else if(f.equals("hour")) {
					date.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY)+amount);
				}else if(f.equals("minute")) {
					date.set(Calendar.MINUTE, date.get(Calendar.MINUTE)+a);
				}else if(f.equals("second")){
					date.set(Calendar.SECOND, date.get(Calendar.SECOND)+a);
				}
				scheduleRepeatedEvent(message, phonenum, user, date, field, amount);
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
		timeTracker.schedule(task, getTimeDiff(Calendar.getInstance()), periodToRepeat);
	}

	//input the date and time at which message should be sent
	//returns the number of milliseconds between the current time and the time it should be sent
	public long getTimeDiff(Calendar cal) {
		Clock clockMain = Clock.system(ZoneId.of("UTC"));
		Clock clock = Clock.offset(clockMain, Duration.ofHours(-7));
		
		Instant start = Instant.now(clock);

        CharSequence cs = cal.toInstant().toString();
		Instant end = Instant.parse(cs);
		
		return Duration.between(start, end).toMillis();
	}
	
	public TimerTask getTask() {
		return task;
	}
	
	public String getMessage() {
		return message;
	}

	public String getTime(){ // Format as a YYYY-MM-DD HH:mm:ss string
		return String.format("%04d-%02d-%02d %02d:%02d:%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), date.get(Calendar.SECOND)); // Format as YYYY-MM-DD HH:mm:ss string
	}
}
