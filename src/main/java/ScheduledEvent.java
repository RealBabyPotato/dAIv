import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.twilio.type.PhoneNumber;

import com.google.gson.annotations.Expose;

import java.time.*;

interface Task{
	void execute();
}

public class ScheduledEvent{
	
	protected Timer timeTracker;

	@Expose
	protected TimerTask task;

	@Expose 
	protected String message;

	private Calendar date;
	private String field;
	private int amount;
	//private PhoneNumber phonenum;
	private String phonenum;
	private User user;

	@Expose
	protected int repeat = -1;

	protected ScheduledEvent(){
		System.out.println("Constructing protected ScheduledEvent (hopefully this should be coming from a subclass!)");
	}
	
	//for one time tasks
	public ScheduledEvent(String m, PhoneNumber n, User u, Calendar d) {
		
		message = m;
	//	phonenum = n;
		user = u;
		date = d;
		
	//	scheduleEvent(message, phonenum, user, date);
	}

	//for repeated events
	public ScheduledEvent(String m, PhoneNumber n, User u, Calendar d, String f, int a) {
		message = m;
	//	phonenum = n;
		user = u;
		date = d;
		field = f;
		amount = a;
		
	//	scheduleRepeatedEvent(message, phonenum, user, date, field, amount);
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

	public int getRepeat(){
		return this.repeat;
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
					date.set(Calendar.MINUTE, date.get(Calendar.MINUTE)+amount);
				}else if(f.equals("second")){
					date.set(Calendar.SECOND, date.get(Calendar.SECOND)+amount);
				}
				//scheduleRepeatedEvent(message, phonenum, user, date, field, amount);
			}
		};
		
		timeTracker = new Timer();
		timeTracker.schedule(task, fixTime(date));
	}

	// temporary constructor for immediate-start scheduled events
	/*public ScheduledEvent(long periodToRepeat, String prompt){ // (final Task runnableTask)
		this.repeat = (int)periodToRepeat;

		this.task = new TimerTask() {
			@Override
			public void run() {
				// runnableTask.execute();
				System.out.println(prompt);
			}
		};

		timeTracker = new Timer();
		timeTracker.scheduleAtFixedRate(this.task, 0, periodToRepeat);
		try{
			timeTracker.schedule(task, getTimeDiff(Calendar.getInstance()), periodToRepeat);
		} catch(IllegalStateException e){
			System.out.println("Attempting to schedule task with negative time difference, ignoring");
		}
	}*/

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

	//allows repeating events to keep running after a restart
	public long fixTime(Calendar cal) {
		Clock clocka = Clock.system(ZoneId.of("UTC"));
		Clock clock = Clock.offset(clocka, Duration.ofHours(-7));
		
		Instant start = Instant.now(clock);
		
		String s = cal.toInstant().toString();
		CharSequence cs = s;
		Instant end = Instant.parse(cs);

		if(getTimeDiff(cal) < 0) {
			while(Duration.between(start, end).toMillis() < 0) {
				if(field == "second") {
					cal.set(Calendar.SECOND, (int) (cal.get(Calendar.SECOND)+amount));
				}
				if(field == "minute") {
					cal.set(Calendar.MINUTE, (int) (cal.get(Calendar.MINUTE)+amount));
				}
				if(field == "hour") {
					cal.set(Calendar.HOUR_OF_DAY, (int) (cal.get(Calendar.HOUR_OF_DAY)+amount));
				}
				if(field == "day") {
					cal.set(Calendar.DAY_OF_YEAR, (int) (cal.get(Calendar.DAY_OF_YEAR)+amount));
				}
				if(field == "week") {
					cal.set(Calendar.WEEK_OF_YEAR, (int) (cal.get(Calendar.WEEK_OF_YEAR)+amount));
				}
				if(field == "month") {
					cal.set(Calendar.MONTH, (int) (cal.get(Calendar.MONTH)+amount));
				}
				if(field == "year") {
					cal.set(Calendar.YEAR, (int) (cal.get(Calendar.YEAR)+amount));
				}
				s = cal.toInstant().toString();
				cs = s;
				end = Instant.parse(cs);
			}
		}
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

class RepeatedEvent extends ScheduledEvent{
	public RepeatedEvent(long periodToRepeat, String prompt){ // (final Task runnableTask)
		this.repeat = (int)periodToRepeat;
		this.message = prompt;

		this.task = new TimerTask() {
			@Override
			public void run() {
				// runnableTask.execute();
				System.out.println(prompt);
			}
		};

		timeTracker = new Timer();
		timeTracker.scheduleAtFixedRate(this.task, 0, periodToRepeat);
		try{
			timeTracker.schedule(task, getTimeDiff(Calendar.getInstance()), periodToRepeat);
		} catch(IllegalStateException e){
			System.out.println("Attempting to schedule task with negative time difference, ignoring");
		}
	}

}
