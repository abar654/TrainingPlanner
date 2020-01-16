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

	/*
	 * Methods for handling the calendar
	 */
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

	/*
	 * Methods for handling HealthConditions
	 */
	
	public void addCondition(HealthCondition newCondition) {
		state.addCondition(newCondition);
	}

	public void removeCondition(HealthCondition toRemove) {
		state.removeCondition(toRemove);
	}
	
	public ArrayList<HealthCondition> getConditions(LocalDate focusDate) {
		return state.getConditions(focusDate);
	}
	
	public HealthCondition getConditionById(long conditionId) {
		return state.getConditionById(conditionId);
	}
	
	/*
	 * Methods for handling HealthReports
	 */
	
	public HealthReport getReportById(long reportId) {
		return state.getReportById(reportId);
	}

	public void removeReport(HealthReport toRemove) {
		state.removeReport(toRemove);
	}

	public void addReport(HealthReport toAdd) {
		state.addReport(toAdd);
	}
	
	/*
	 * Methods for handling AccountDetails
	 */
	
	public AccountDetails getDetails() {
		return details;
	}

	/*
	 * Methods for handling Recommendations
	 */
	
	public ArrayList<Recommendation> getRecommendations(LocalDate focusDate) {
		return Recommendation.makeRecommendations(getTrainingWeek(focusDate), getConditions(focusDate));
	}

}
