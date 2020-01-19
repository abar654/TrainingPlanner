import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

public class HealthReport {
	
	private LocalDate date;
	private int vasRating; //A rating from 1 to 10, 10 being the worst
	private String comment;
	private boolean isSerious;
	private long id;
	private long sessionId;
	private long conditionId;

	public HealthReport(LocalDate date, int rating, String comment, boolean serious, long conditionId, long sessionId,
			long reportId) {
		
		this.date = date;
		this.vasRating = rating;
		this.comment = comment;
		this.isSerious = serious;
		this.id = reportId;
		this.conditionId = conditionId;
		this.sessionId = sessionId;		
		
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public int getRating() {
		return vasRating;
	}
	
	public String getComment() {
		return comment;
	}

	public Long getId() {
		return id;
	}

	public long getSessionId() {
		return sessionId;
	}
	
	public long getConditionId() {
		return conditionId;
	}

	public boolean isSerious() {
		return isSerious;
	}
	
	public void setDate(LocalDate newDate) {
		date = newDate;
	}
	
	public void setRating(int newRating) {
		vasRating = newRating;
	}
	
	public void setComment(String newComment) {
		comment = newComment;
	}
	
	public void setSerious(boolean newSerious) {
		isSerious = newSerious;
	}
	
	public void setSessionId(long newSessionId) {
		sessionId = newSessionId;
	}

}
