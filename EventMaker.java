package daiv;

import java.util.Calendar;

public class EventMaker {

	public static void makeEvent(String r, Calendar d) {
		ScheduledEvent a = new ScheduledEvent(r, d);
	}
	
	public static void makeEvent(String r, Calendar d, long rep) {
		ScheduledEvent a = new ScheduledEvent(r, d, rep);
	}
	
}
