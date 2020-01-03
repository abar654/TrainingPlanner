import java.time.DayOfWeek;
import java.time.LocalDate;
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

	/*
	 * Finds all the sessions in the week surrounding focusDate, 
	 * as well as the acute loads for the previous weeks, 
	 * and returns them in a TrainingWeek object.
	 */
	public TrainingWeek getTrainingWeek(LocalDate focusDate) {
		
		//Find the date that the training week starts
		//i.e. the monday of that week (perhaps make this configurable later)
		LocalDate startDate = focusDate;
		while(startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
			startDate = startDate.minusDays(1);
		}
		
		//Find all sessions that are from this date up to this date + 6.
		//Also calculate the acute loads for previous weeks.
		ArrayList<Session> weekSessions = new ArrayList<Session>();
		int[] acuteLoads = {0, 0, 0, 0}; //Note 0 index is most recent week, 3 index is earliest week.
		
		for(Session calendarSession: sessions) {
			
			LocalDate sessionDate = calendarSession.getDate();
			
			if(sessionDate.isAfter(startDate.minusDays(1)) 
					&& sessionDate.isBefore(startDate.plusDays(7))) {
				weekSessions.add(calendarSession);
			}
			
			if(sessionDate.isBefore(startDate)) {
				
				if(sessionDate.isAfter(startDate.minusDays(8))) {
					//Add to week 0 load
					acuteLoads[0] += calendarSession.getLoad();
				} else if(sessionDate.isAfter(startDate.minusDays(15))) {
					//Add to week 1 load
					acuteLoads[1] += calendarSession.getLoad();
				} else if(sessionDate.isAfter(startDate.minusDays(22))) {
					//Add to week 2 load
					acuteLoads[2] += calendarSession.getLoad();
				} else if(sessionDate.isAfter(startDate.minusDays(29))) {
					//Add to week 3 load
					acuteLoads[3] += calendarSession.getLoad();
				}
				
			}
			
		}
		
		//Create the Training Week object
		TrainingWeek newWeek = new TrainingWeek(weekSessions, startDate, acuteLoads);
		
		return newWeek;
	}

}
