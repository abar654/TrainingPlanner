import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

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

	public void addSession(Session newSession) {
		calendar.addSession(newSession);
	}

	public void addCondition(HealthCondition newCondition) {
		state.addCondition(newCondition);
	}

	public void removeSession(Session toRemove) {
		calendar.removeSession(toRemove);		
	}

	public void removeCondition(HealthCondition toRemove) {
		state.removeCondition(toRemove);
	}
	
	public AccountDetails getDetails() {
		return details;
	}

	public Session getSessionById(long sessionId) {
		return calendar.getSessionById(sessionId);
	}

	public TrainingWeek getTrainingWeek(LocalDate focusDate) {
		return calendar.getTrainingWeek(focusDate);	
	}
	
	public ArrayList<HealthCondition> getConditions(LocalDate focusDate) {
		return state.getConditions(focusDate);
	}
	
	public ArrayList<Recommendation> getRecommendations(LocalDate focusDate) {
		return Recommendation.makeRecommendations(getTrainingWeek(focusDate), getConditions(focusDate));
	}

}
