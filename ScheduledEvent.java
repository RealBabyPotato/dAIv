package daiv;

import java.util.Timer;
import java.util.TimerTask;


public class ScheduledEvent{
	
	private Timer timeTracker;
	private TimerTask task;
	private long timeToSend;
	private String response;
	private int identifier;
	
	public ScheduledEvent(int id, String r, long d) {
		
		identifier = id;
		response = r;
		timeToSend = d;
		
		task = new TimerTask() {
			public void run() {
				//do the do
				//this is a test
				System.out.println("aaaaaa");
			}
		};
		
		//get timeToSend by asking for milliseconds from chat
		timeTracker = new Timer();
		timeTracker.schedule(task, timeToSend);
		
	}
	
	
	
	
}
