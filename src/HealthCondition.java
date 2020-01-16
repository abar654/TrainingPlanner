import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class HealthCondition {
	
	private String name;
	private LocalDate startDate;
	private LocalDate endDate; //Equals null if the condition is still active
	private boolean isIllness;
	private long id;
	private ArrayList<HealthReport> reports;
	private ArrayList<Sport> bannedSports;

	public HealthCondition(String name, LocalDate date, boolean illness, long conditionId) {
		
		this.name = name;
		startDate = date;
		endDate = null;
		isIllness = illness;
		id = conditionId;
		
		reports = new ArrayList<HealthReport>();
		bannedSports = new ArrayList<Sport>();		
		
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public long getDuration() {
		return startDate.until(endDate, ChronoUnit.DAYS);
	}
	
	public long getId() {
		return id;
	}
	
	public ArrayList<HealthReport> getReports() {
		ArrayList<HealthReport> copy = new ArrayList<HealthReport>();
		if(!reports.isEmpty()) {
			copy.addAll(reports);
		}
		return copy;
	}
	
	public ArrayList<Sport> getBannedSports() {
		ArrayList<Sport> copy = new ArrayList<Sport>();
		if(!bannedSports.isEmpty()) {
			copy.addAll(bannedSports);
		}
		return copy;
	}
	
	public boolean isIllness() {
		return isIllness;
	}
	
	public boolean isSerious() {
		//Check the most recent report to see if the condition is serious
		HealthReport mostRecent = getMostRecentReport();
		boolean serious = false;
		if(mostRecent != null ) {
			serious = mostRecent.isSerious();
		}
		return serious;
	}
	
	void setStartDate(LocalDate newStart) {
		startDate = newStart;
	}
	
	void setEndDate(LocalDate newEnd) {
		endDate = newEnd;
	}
	
	void setName(String newName) {
		name = newName;
	}

	public void addBannedSport(Sport sport) {
		bannedSports.add(sport);
	}
	
	public void removeBannedSport(Sport sport) {
		bannedSports.remove(sport);
	}

	public void addReport(HealthReport toAdd) {
		reports.add(toAdd);		
	}

	public void removeReport(HealthReport toRemove) {
		reports.remove(toRemove);		
	}

	public HealthReport getReportById(long reportId) {
		for(HealthReport report : reports) {
			if(report.getId() == reportId) {
				return report;
			}
		}
		return null;
	}
	
	private HealthReport getMostRecentReport() {
		
		LocalDate mostRecentDate = LocalDate.MIN;
		HealthReport mostRecentReport = null;
		
		for(HealthReport report : reports) {
			if(mostRecentDate.isBefore(report.getDate())) {
				mostRecentDate = report.getDate();
				mostRecentReport = report;
			}
		}
		
		return mostRecentReport;
	}

}
