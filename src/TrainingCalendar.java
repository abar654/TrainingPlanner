import java.util.ArrayList;

public class TrainingCalendar {
	
	private ArrayList<Session> sessions;
	
	public TrainingCalendar() {
		sessions = new ArrayList<Session>();
	}

	public void addSession(Session newSession) {
		sessions.add(newSession);		
	}

	public void removeSession(Session toRemove) {
		sessions.remove(toRemove);
	}

	/*
	 * Finds a session with the id provided and if no such session exists
	 * then the function returns null.
	 */
	public Session getSessionById(long sessionId) {
		for(Session existingSession: sessions) {
			if(existingSession.getId() == sessionId) {
				return existingSession;
			}
		}
		return null;
	}

}
