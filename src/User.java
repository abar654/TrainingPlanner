import java.io.File;

public class User {
	
	private AccountDetails details;
	private TrainingCalendar calendar;
	private HealthState state;
	

	public User(AccountDetails userDetails) {
		details = userDetails;
		calendar = new TrainingCalendar();
		state = new HealthState();
	}


	public AccountDetails getDetails() {
		return details;
	}


	public String toJSONStr() {
		
		String returnString = "";
		
		//Write the AccountDetails object
		returnString += details.toJSONStr();
		
		//Write the TrainingCalendar object
		returnString += calendar.toJSONStr();
		
		//Write the HealthState object
		returnString += state.toJSONStr();
		
		return returnString;
	}

}
