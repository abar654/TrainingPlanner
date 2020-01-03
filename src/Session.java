import java.time.LocalDate;
import java.util.ArrayList;

public class Session {
	
	private Sport sport;
	private String sessionType;
	private double distance; //in kms
	private int duration; //in minutes
	private int intensity; //from 1 to 10 inclusive
	private String comment; //max 1000 chars
	private boolean completed;
	private long id;
	private ArrayList<ConditionReport> reports;
	private LocalDate date;

	public Session(LocalDate date, Sport sport, String sessionType, double distance, int duration, int intensity, String comment,
			boolean completed, long id) {

		this.date = date;
		this.sport = sport;
		this.sessionType = sessionType;
		this.distance = distance;
		this.duration = duration;
		this.intensity = intensity;
		this.comment = comment;
		this.completed = completed;
		this.id = id;
		reports = new ArrayList<ConditionReport>();

	}
	
	public Sport getSport() {
		return sport;
	}
	
	public long getId() {
		return id;
	}
	
	public String getSessionType() {
		return sessionType;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getIntensity() {
		return intensity;
	}
	
	public String getComment() {
		return comment;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public int getLoad() {
		return duration * intensity;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	/*
	 * Returns a shallow copy of the reports
	 */
	public ArrayList<ConditionReport> getReports() {
		ArrayList<ConditionReport> copy = new ArrayList<ConditionReport>();
		if(!reports.isEmpty()) {
			copy.addAll(reports);
		}
		return copy;
	}
	
	public void setSport(Sport newSport) {
		sport = newSport;
		
		//Check that the sessionType is still valid
		boolean typeFound = false;
		
		for(String existingType: newSport.getSessionTypes()) {
			if(existingType.equals(sessionType)) {
				typeFound = true;
				break;
			}
		}
		
		if(!typeFound) {
			sessionType = "Unknown";
		}
	}
	
	public void setSessionType(String newType) {
		sessionType = newType;
	}
	
	public void setDistance(double newDistance) {
		distance = newDistance;
	}
	
	public void setDuration(int newDuration) {
		duration = newDuration;
	}
	
	public void setIntensity(int newIntensity) {
		intensity = newIntensity;
	}
	
	public void setComment(String newComment) {
		comment = newComment;
	}
	
	public void setCompleted(boolean newCompleted) {
		completed = newCompleted;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public void addReport(ConditionReport toAdd) {
		reports.add(toAdd);
	}
	
	public void removeReport(ConditionReport toRemove) {
		reports.remove(toRemove);
	}

}
