import java.io.File;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class User {
	
	private AccountDetails details;
	private TrainingCalendar calendar;
	private HealthState state;
	

	public User(AccountDetails userDetails) {
		details = userDetails;
		calendar = new TrainingCalendar();
		state = new HealthState();
	}


	public AccountDetails getDetails() {
		return details;
	}


	public void addSession(Session newSession) {
		calendar.addSession(newSession);
	}


	public void removeSession(Session toRemove) {
		calendar.removeSession(toRemove);		
	}


	public Session getSessionById(long sessionId) {
		return calendar.getSessionById(sessionId);
	}


	public TrainingWeek getTrainingWeek(LocalDate focusDate) {
		return calendar.getTrainingWeek(focusDate);	
	}

}
