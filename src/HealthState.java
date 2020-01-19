import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class HealthState {
	
	ArrayList<HealthCondition> conditions;
	HashMap<Long, Long> reportIdToConditionId;
	
	public HealthState() {
		
		//Initialise the collections
		conditions = new ArrayList<HealthCondition>();
		reportIdToConditionId = new HashMap<Long, Long>();
		
	}
	
	/*
	 * Methods to handle conditions
	 */

	public void addCondition(HealthCondition newCondition) {
		conditions.add(newCondition);		
	}

	public void removeCondition(HealthCondition toRemove) {
		
		//Remove the condition from the condition list
		conditions.remove(toRemove);
		
		//Remove all entries in the reportIdtoConditionId
		for(HealthReport report : toRemove.getReports()) {
			reportIdToConditionId.remove(report.getId());
		}
		
	}

	/*
	 * Gets the conditions for the week that includes focusDate.
	 * If the condition has an endDate after the last day of this week,
	 * then we return it.
	 */
	public ArrayList<HealthCondition> getConditions(LocalDate focusDate) {
				
		//Create a new list to hold the conditions active in this week
		ArrayList<HealthCondition> weekConditions = new ArrayList<HealthCondition>();
		
		//Find the endDate for this week
		LocalDate endDate = focusDate;
		while(endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
			endDate = endDate.plusDays(1);
		}
		
		//Go through all conditions, add if its endDate is null or after endDate
		//and it's start date is before or equal endDate
		for(HealthCondition condition : conditions) {
			if((condition.getEndDate() == null || condition.getEndDate().isAfter(endDate))
					&& (condition.getStartDate().isBefore(endDate) || condition.getStartDate().isEqual(endDate))) {
				weekConditions.add(condition);
			}
		}
		
		return weekConditions;
		
	}
	
	public HealthCondition getConditionById(long conditionId) {
		for(HealthCondition condition : conditions) {
			if(condition.getId() == conditionId) {
				return condition;
			}
		}
		return null;
	}
	
	/*
	 * Methods to handle reports
	 */

	public void addReport(HealthReport toAdd) {
		
		long conditionId = toAdd.getConditionId();
		
		//Add the reportId to the map so it can be found later
		reportIdToConditionId.put(toAdd.getId(), conditionId);
		
		//Add the report to its relevant condition
		getConditionById(conditionId).addReport(toAdd);
		
	}
	
	public void removeReport(HealthReport toRemove) {
		
		//Remove the report from the condition
		long conditionId = toRemove.getConditionId();
		getConditionById(conditionId).removeReport(toRemove);
		
		//Remove the report from the look up map
		reportIdToConditionId.remove(toRemove.getId());
		
	}
	
	public HealthReport getReportById(long reportId) {
		
		//Get the condition
		HealthCondition condition = getConditionById(reportIdToConditionId.get(reportId));
		
		//Get the report
		return condition.getReportById(reportId);
		
	}

}
