import java.util.ArrayList;

public class TrainingCalendar {
	
	private ArrayList<Session> sessions;
	
	public TrainingCalendar() {
		sessions = new ArrayList<Session>();
	}

	public void addSession(Session newSession) {
		sessions.add(newSession);		
	}

}
