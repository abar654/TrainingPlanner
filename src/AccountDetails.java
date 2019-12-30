import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A class that manages all the account details for a user in the TrainingPlanner
 * @author Andrew
 *
 */

public class AccountDetails {
	
	private String username;
	private String name;
	private LocalDate dob;
	private String email;
	private Sport primarySport;
	private ArrayList<Sport> sports; //Note that sessionTypes are now stored within each sport
	private long nextSessionId;
	

	/*
	 * Creates a new AccountDetails object containing all the provided information
	 */
	public AccountDetails(String username, String name, LocalDate date, String email, String sport) {
		
		this.username = username;
		this.name = name;
		this.dob = date;
		this.email = email;
		
		primarySport = new Sport(sport, 0);
		
		sports = new ArrayList<Sport>();
		sports.add(primarySport);
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDate getDOB() {
		return dob;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Sport getPrimarySport() {
		return primarySport;
	}
	
	/*
	 * Returns a shallow copy of the sports list
	 */
	public ArrayList<Sport> getSports() {
		ArrayList<Sport> copy = new ArrayList<Sport>();
		copy.addAll(sports);
		return copy;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public void setDOB(LocalDate newDate) {
		dob = newDate;
	}
	
	public void setEmail(String newEmail) {
		email = newEmail;
	}
	
	/*
	 * newPrimary must be a sport which is already added to the account.
	 */
	public void setPrimarySport(Sport newPrimary) {
		primarySport = newPrimary;
	}
	
	public void addSport(Sport newSport) {
		sports.add(newSport);
	}

}
