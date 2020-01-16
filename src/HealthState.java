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
		conditions.remove(toRemove);		
	}

	public ArrayList<HealthCondition> getConditions(LocalDate focusDate) {
		ArrayList<HealthCondition> copy = new ArrayList<HealthCondition>();
		if(!conditions.isEmpty()) {
			copy.addAll(conditions);
		}
		return copy;
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
