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
		
		//Remove from the calendar
		calendar.removeSession(toRemove);
		
		//Remove its associated HealthReports
		for(Long reportId : toRemove.getReports()) {
			
			HealthReport report = getReportById(reportId);
			
			if(report != null) {
				removeReport(report);
			}
			
		}
		
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
		
		//Remove from the HealthState
		state.removeCondition(toRemove);
		
		//Remove all the HealthReports in this condition from their sessions
		for(HealthReport report : toRemove.getReports()) {
			
			//Remove this report from its session
			Session removeFrom = getSessionById(report.getSessionId());
			if(removeFrom != null) {
				removeFrom.removeReport(report.getId());
			}
			
		}
		
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
		
		//Remove from the HealthState
		state.removeReport(toRemove);
		
		//Remove from its session
		Session removeFrom = getSessionById(toRemove.getSessionId());
		if(removeFrom != null) {
			removeFrom.removeReport(toRemove.getId());
		}
		
	}

	public void addReport(HealthReport toAdd) {
		
		//Add to the correct session
		//If the session does not exist then we shouldn't add the report
		Session addTo = getSessionById(toAdd.getSessionId());
		if(addTo != null) {
			
			addTo.addReport(toAdd.getId());
			
			//Add to the health state
			state.addReport(toAdd);
			
		} else if(toAdd.getSessionId() == -1) {
			
			//This is a sessionless report
			//Add to the health state
			state.addReport(toAdd);
			
		}
		
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
