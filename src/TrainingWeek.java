import java.time.LocalDate;
import java.util.ArrayList;

public class TrainingWeek {
	
	private ArrayList<Session> sessions;
	private LocalDate start;
	private int[] prevAcuteLoads; //Note 0 index is most recent week, 3 index is earliest week.

	public TrainingWeek(ArrayList<Session> weekSessions, LocalDate startDate, int[] acuteLoads) {
		
		sessions = weekSessions;
		start = startDate;
		prevAcuteLoads = acuteLoads;
		
	}

	public LocalDate getStartDate() {
		return start;
	}

	/*
	 * Returns the chronic load for this week.
	 * Chronic load is calculated as the average of the acute loads for the
	 * previous 4 weeks.
	 */
	public int getChronicLoad() {
		
		int load = 0;
		
		for(int i = 0; i < prevAcuteLoads.length; i++) {
			load += prevAcuteLoads[i];
		}
		
		return load/prevAcuteLoads.length;
		
	}

	/*
	 * Returns a shallow copy of the previousAcuteLoads
	 */
	public int[] getPrevAcuteLoads() {
		return prevAcuteLoads.clone();
	}

	/*
	 * Returns the acute load for the week.
	 * Acute load is the sum of the loads for each session in the week.
	 */
	public int getAcuteLoad() {
		
		int load = 0;
		
		for(Session session: sessions) {
			load += session.getLoad();
		}
		
		return load;
		
	}

	/*
	 * Returns to sum of the distance trained for the given sport.
	 */
	public double getWeekDistance(Sport sport) {
		
		double distance = 0;
		
		for(Session session: sessions) {
			if(session.getSport().equals(sport)) {
				distance += session.getDistance();
			}
		}
		
		return distance;
		
	}

	/*
	 * Returns the sum of the training times for all sessions in the week.
	 */
	public int getWeekTime() {
		
		int minutes = 0;
		
		for(Session session: sessions) {
			minutes += session.getDuration();
		}
		
		return minutes;
	}

	/*
	 * Returns the Acute:Chronic Workload Ratio.
	 * This is calculated as acute load / chronic load.
	 */
	public double getACWR() {
		return getAcuteLoad()*1.0/getChronicLoad();
	}

	/*
	 * Returns the status of the ACWR.
	 * Status is "OK" if ACWR is between 0.8 and 1.25.
	 * Status is "CAUTION" if ACWR is outside of these bounds.
	 */
	public String getACWRStatus() {
		double acwr = getACWR();
		if(acwr >= 0.8 && acwr <= 1.25) {
			return "OK";
		}
		return "CAUTION";
	}

}
