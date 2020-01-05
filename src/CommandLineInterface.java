import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommandLineInterface {
	
	private Scanner input;
	private TrainingPlanner tp;

	public CommandLineInterface(Scanner input, TrainingPlanner trainingPlanner) {
		this.input = input;
		tp = trainingPlanner;
	}

	public User createNewUser() {
		
		System.out.println("Valid username not provided - create a new user");
		
		//Collect the required information for a new user
		//Get the name
		System.out.print("Enter a name: ");
		String name = input.nextLine().trim();
		
		//Get the date of birth
		System.out.print("Enter your date of birth (" + TrainingPlanner.DATE_FORMAT + "): ");
		boolean dateSuccess = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TrainingPlanner.DATE_FORMAT);
		LocalDate date = null;
		
		while(!dateSuccess) {
			
			String text = input.nextLine().trim();
			
			try {
				date = LocalDate.parse(text, formatter);
				dateSuccess = true;
			} catch (DateTimeParseException e) {
				System.out.print("Date error. Please enter date in format (" + TrainingPlanner.DATE_FORMAT + "): ");
			}
			
		}
		
		//Get primary sport
		System.out.print("Enter primary sport: ");
		Sport sport = new Sport(input.nextLine().trim(), new Color(0));
		
		//Get email			
		System.out.print("Enter email: ");
		String email = input.nextLine().trim();
		
		//Get username, but also check that the username is not taken and contains only alphanumerics
		System.out.print("Enter a username: ");
		boolean usernameSuccess = false;
		String username = null;
		
		while(!usernameSuccess) {
			
			username = input.nextLine().trim();
			
			if(tp.isValidUsername(username)) {
				usernameSuccess = true;
			} else {
				System.out.println("Username unavailable. Make sure to use only letters, numbers or underscores (6 to 18 characters).");
				System.out.print("Please enter another username: ");
			}
			
		}
		
		//Create the new User's AccountDetails object
		AccountDetails userDetails = new AccountDetails(username, name, date, email, sport);
		
		//Create the new User
		return new User(userDetails);
		
	}

	public void run(User currentUser) {
		
		boolean shutdown = false;
		
		//Set the focus date to right now
		LocalDate focusDate = LocalDate.now();
		
		//The command line options
		while(!shutdown) {
		
			//Print prompt
			System.out.print(currentUser.getDetails().getName() + ">>");
			//Read the next command
			String nextCommand = input.nextLine().trim();
			
			if(nextCommand.equals("show-user")) {
				
				//Show user details
				showUser(currentUser);
			
			} else if(nextCommand.equals("add-session")) {
				
				//Add a session
				addSession(currentUser);
				
			} else if(nextCommand.equals("add-condition")) {
				
				//Add a condition
				addCondition(currentUser);
				
			} else if(nextCommand.equals("add-report")) {
				
				//Add a HealthReport
				addReport(currentUser);
				
			} else if(nextCommand.equals("remove-session")) {
				
				//Remove a session
				removeSession(currentUser);
				
			} else if(nextCommand.equals("remove-condition")) {
				
				//Remove a condition
				removeCondition(currentUser);
				
			} else if(nextCommand.equals("remove-report")) {
				
				//Remove a health report
				removeReport(currentUser);
				
			} else if(nextCommand.equals("set-date")) {
				
				//Set focus date
				focusDate = setDate();
				
			} else if(nextCommand.equals("show-sessions")) {
				
				//Show current training week sessions
				showSessions(currentUser, focusDate);
				
			} else if(nextCommand.equals("show-data")) {
				
				//Show current training week data
				showData(currentUser, focusDate);
				
			} else if(nextCommand.equals("show-conditions")) {
				
				//Show conditions for current training week
				showConditions(currentUser, focusDate);
				
			} else if(nextCommand.equals("show-recommendations")) {
				
				//Show recommendations based on current training week
				//To be implemented in stage 4
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("save")) {
				
				//Saves current user state to file.
				tp.writeUser(currentUser);
				System.out.println("User saved.");
				
			} else if(nextCommand.equals("help")) {
				
				//Help. Prints a list of all the commands and their functions.
				System.out.println("Training Planner Commands:\n"
								+ "\ncommand: description\n\n"
								+ "set-date: Set focus date\n"
								+ "show-user: Show user details\n"
								+ "show-sessions: Show current training week sessions\n"
								+ "show-data: Show current training week data\n"
								+ "show-conditions: Show conditions for current training week\n"
								+ "show-recommendations: Show recommendations based on current training week\n"
								+ "add-session: Add a session\n"
								+ "add-condition: Add a condition\n"
								+ "add-report: Add a health report\n"
								+ "remove-session: Remove a session\n"
								+ "remove-condition: Remove a condition\n"
								+ "remove-report: Remove a health report\n"
								+ "save: Saves current user state to file\n"
								+ "help: Show this list of commands\n"
								+ "exit: Shut down the program\n");
				
			} else if(nextCommand.equals("exit")) {
				
				//Exit
				System.out.print("Are you sure you wish to exit? (Y/N) ");
				String response = input.nextLine().trim();
				
				if(response.toLowerCase().equals("y")) {
					shutdown = true;
					input.close();
				}
				
			} else {
				
				//Command not recognised
				System.out.println("Command not recognised. Type 'help' to see list"
						+ " of commands or 'exit' to exit.");
				
			}
			
		}
		
	}

	private void showConditions(User currentUser, LocalDate focusDate) {

		//Get conditions from the user for the week of focusDate.
		//This will return conditions that will currently be active
		//at the end of the week in which focusDate lies.
		//Print out using GSON.
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(currentUser.getConditions(focusDate)));
		
	}

	private void showData(User currentUser, LocalDate focusDate) {
		//Get the data for the week in focus
		TrainingWeek week = currentUser.getTrainingWeek(focusDate);
		
		//Print out the different pieces of data
		
		//Start date
		LocalDate startDate = week.getStartDate();
		System.out.println("Data for week starting: " + startDate.getDayOfMonth()
				+ " " + startDate.getMonthValue() + " " + startDate.getYear());
		
		//Chronic load
		System.out.println("Chronic load: " + week.getChronicLoad());
		
		//Previous acute loads
		int[] prevLoads = week.getPrevAcuteLoads();
		System.out.println("Previous acute loads: " + prevLoads[0] + " " +
				prevLoads[1] + " " + prevLoads[2] + " " + prevLoads[3]);
		
		//Acute load
		System.out.println("Current acute load: " + week.getAcuteLoad());
		
		//Week distance
		System.out.println("Primary sport distance: " 
		+ week.getWeekDistance(currentUser.getDetails().getPrimarySport()));
		
		//Week time
		System.out.println("Training time: " + week.getWeekTime());
		
		//ACWR
		System.out.println("Acute:Chronic Workload Ratio: " + week.getACWR());
		
		//ACWR Status
		System.out.println("Current ACWR status: " + week.getACWRStatus());

	}

	private void showSessions(User currentUser, LocalDate focusDate) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(currentUser.getTrainingWeek(focusDate)));
		
	}
	
	private void showUser(User currentUser) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(currentUser.getDetails()));
		
	}

	private LocalDate setDate() {
		
		System.out.print("Enter a date during the week you would like to show: ");
		boolean dateSuccess = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TrainingPlanner.DATE_FORMAT);
		
		LocalDate focusDate = null;
		
		while(!dateSuccess) {
			
			String text = input.nextLine().trim();
			
			try {
				focusDate = LocalDate.parse(text, formatter);
				dateSuccess = true;
			} catch (DateTimeParseException e) {
				System.out.print("Date error. Please enter date in format (" + TrainingPlanner.DATE_FORMAT + "): ");
			}
			
		}
		
		return focusDate;
		
	}

	private void removeSession(User currentUser) {
		
		//Get the sessionId from the user
		System.out.print("Enter the session id for the session you would like to remove: ");
		long sessionId = -1;
		
		while(sessionId < 0) {
			
			String idInput = input.nextLine().trim();
			
			try {
				sessionId = Long.parseLong(idInput);
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}
		
		//Get the session to be removed
		Session toRemove = currentUser.getSessionById(sessionId);
		
		//Call the remove function on the user
		//Check that this session existed
		//getSessionById() should return null if it did not
		if(toRemove != null) {
			currentUser.removeSession(toRemove);
			System.out.println("Session " + sessionId + " removed.");
		} else {
			System.out.println("Session not found.");
		}
		
	}
	
	private void removeReport(User currentUser) {

		//Get the reportId from the user
		System.out.print("Enter the report id for the report you would like to remove: ");
		long reportId = -1;
		
		while(reportId < 0) {
			
			String idInput = input.nextLine().trim();
			
			try {
				reportId = Long.parseLong(idInput);
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}
		
		//Get the report to be removed
		HealthReport toRemove = currentUser.getReportById(reportId);
		
		//Call the remove function on the user
		//Check that this session existed
		//getSessionById() should return null if it did not
		if(toRemove != null) {
			currentUser.removeReport(toRemove);
			System.out.println("Report " + reportId + " removed.");
		} else {
			System.out.println("Report not found.");
		}
		
	}

	private void removeCondition(User currentUser) {
		
		//Get the conditionId from the user
		System.out.print("Enter the condition id for the report you would like to remove: ");
		long conditionId = -1;
		
		while(conditionId < 0) {
			
			String idInput = input.nextLine().trim();
			
			try {
				conditionId = Long.parseLong(idInput);
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}
		
		//Get the report to be removed
		HealthCondition toRemove = currentUser.getConditionById(conditionId);
		
		//Call the remove function on the user
		//Check that this session existed
		//getSessionById() should return null if it did not
		if(toRemove != null) {
			currentUser.removeCondition(toRemove);
			System.out.println("Condition " + conditionId + " removed.");
		} else {
			System.out.println("Condition not found.");
		}
		
	}

	private void addSession(User currentUser) {
		
		//Get all the information to create the session
		
		//Get the date
		System.out.print("Date for this session: ");
		boolean dateSuccess = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TrainingPlanner.DATE_FORMAT);
		LocalDate date = null;
		
		while(!dateSuccess) {
			
			String text = input.nextLine().trim();
			
			try {
				date = LocalDate.parse(text, formatter);
				dateSuccess = true;
			} catch (DateTimeParseException e) {
				System.out.print("Date error. Please enter date in format (" + TrainingPlanner.DATE_FORMAT + "): ");
			}
			
		}
						
		//Get the Sport (if it is a new sport then check that the user wants to create)
		Sport nextSport = null;
		
		while(nextSport == null) {
			
			System.out.print("Sport type for this session: ");
			String sportName = input.nextLine().trim();
			
			for(Sport currentSport: currentUser.getDetails().getSports()) {
				if(currentSport.getName().equals(sportName)) {
					nextSport = currentSport;
					break;
				}
			}
			
			if(nextSport == null) {
				
				//Ask the user if they want to create a new sport
				System.out.print("Create new sport with name '" + sportName + "' (y/n)? ");
				String response = input.nextLine().trim();
				
				if(response.toLowerCase().equals("y")) {
					
					//Create a new sport and add it to the user
					nextSport = new Sport(sportName, new Color(0));
					currentUser.getDetails().addSport(nextSport);

				}
				
			}
			
		}
		
		//Get the SessionType
		System.out.print("Session type for this session: ");
		String typeName = input.nextLine().trim();
		
		//If this sessionType doesn't exist for the Sport then add it to the Sport's sessiontypes
		boolean typeExists = false;
		
		for(String existingType: nextSport.getSessionTypes()) {
			if(existingType.equals(typeName)) {
				typeExists = true;
				break;
			}
		}
		
		if(!typeExists) {
			nextSport.addSessionType(typeName);
		}
		
		//Get distance (0 for stationary)
		System.out.print("Distance for this session in km (enter 0 if stationary session): ");
		double distance = -1;
		
		while(distance < 0) {
			
			String distanceInput = input.nextLine().trim();
			
			try {
				
				//Parse distance and round to 2 dp
				distance = Math.floor(Double.parseDouble(distanceInput)*100 + 0.5)/100;
				if(distance < 0 || distance > 2000) {
					System.out.print("Distance must be positive number in km (less than 2000): ");
					distance = -1;
				}
				
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}		
		
		//Get duration (in minutes)
		System.out.print("Duration for this session in minutes: ");
		int duration = -1;
		
		while(duration < 0) {
			
			String durationInput = input.nextLine().trim();
			
			try {
				
				//Parse duration (round to nearest minute)
				duration = (int) Math.floor(Double.parseDouble(durationInput) + 0.5);
				if(duration < 0 || duration > 60*24) {
					System.out.print("Duration must be positive number in minutes: ");
					duration = -1;
				}
				
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}	
		
		//Get intensity (from 1 to 10)
		System.out.print("Intensity for this session in (RPE scale 1 to 10): ");
		int intensity = -1;
		
		while(intensity < 0) {
			
			String intensityInput = input.nextLine().trim();
			
			try {
				
				//Parse intensity
				intensity = Integer.parseInt(intensityInput);
				if(intensity < 1 || intensity > 10) {
					System.out.print("Intensity must be an integer between 1 and 10 (inclusive): ");
					intensity = -1;
				}
				
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}	
		
		//Get comment (max 1000 chars)
		System.out.print("Comment for this session (max 1000 characters): ");
		String comment = input.nextLine().trim();
		if(comment.length() > 1000) {
			comment = comment.substring(0, 1000);
		}
		
		//Get completed flag (y/n)
		System.out.print("Is this session completed (y/n)? ");
		String response = input.nextLine().trim();
		boolean completed = false;
		
		if(response.toLowerCase().equals("y")) {
			completed = true;
		}
		
		//Make a new session
		Session newSession = new Session(date, nextSport, typeName, distance, duration, 
				intensity, comment, completed, currentUser.getDetails().giveNextSessionId());
		
		//Add it to the current user
		currentUser.addSession(newSession);
		
	}

	private void addReport(User currentUser) {
		
		//Collect all the information to add to the report
		
		//ConditionId
		System.out.print("Enter the condition id for the condition associated with this report: ");
		long conditionId = -1;
		
		while(conditionId < 0) {
			
			String idInput = input.nextLine().trim();
			
			try {
				conditionId = Long.parseLong(idInput);
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}
		
		//VAS Rating - 1 to 10 (1 being best, 10 being worst)
		System.out.print("Rate the status of your condition on a scale of 1 to 10 (10 being the worst): ");
		int rating = -1;
		
		while(rating < 0) {
			
			String ratingInput = input.nextLine().trim();
			
			try {
				
				//Parse rating
				rating = Integer.parseInt(ratingInput);
				if(rating < 1 || rating > 10) {
					System.out.print("Rating must be an integer between 1 and 10 (inclusive): ");
					rating = -1;
				}
				
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}
		
		//Get comment (max 2000 chars)
		System.out.print("Comment on the status of your condition (max 2000 characters): ");
		String comment = input.nextLine().trim();
		if(comment.length() > 2000) {
			comment = comment.substring(0, 2000);
		}
		
		//Get seriousness
		System.out.print("Is the condition serious (y/n)? ");
		String response = input.nextLine().trim();
		boolean serious = false;
		
		if(response.toLowerCase().equals("y")) {
			serious = true;
		}
		
		//Get the sessionId to relate it to
		System.out.print("Enter the session id for the session associated with this report: ");
		long sessionId = -1;
		
		while(sessionId < 0) {
			
			String idInput = input.nextLine().trim();
			
			try {
				sessionId = Long.parseLong(idInput);
			} catch(NumberFormatException e) {
				System.out.print("Please enter a number: ");
			}
			
		}
		
		//Get the date from the session
		LocalDate date = currentUser.getSessionById(sessionId).getDate();
		
		//Get a reportId from AccountDetails
		long reportId = currentUser.getDetails().giveNextReportId();
		
		//Create the HealthReport object
		HealthReport toAdd = new HealthReport(date, rating, comment, 
				serious, conditionId, sessionId, reportId);
		
		//Add the HealthReport to the HealthState
		currentUser.addReport(toAdd);
		
	}

	private void addCondition(User currentUser) {
		
		//Collect all the information about the condition
		
		//Name
		System.out.print("Name for this condition: ");
		String name = input.nextLine().trim();
		
		//Start date
		//Get the date
		System.out.print("Start date for this condition: ");
		boolean dateSuccess = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TrainingPlanner.DATE_FORMAT);
		LocalDate date = null;
		
		while(!dateSuccess) {
			
			String text = input.nextLine().trim();
			
			try {
				date = LocalDate.parse(text, formatter);
				dateSuccess = true;
			} catch (DateTimeParseException e) {
				System.out.print("Date error. Please enter date in format (" + TrainingPlanner.DATE_FORMAT + "): ");
			}
			
		}
		
		//Banned Sports
		//Print out the current sports
		System.out.print("Your current sports are: ");
		for(Sport sport : currentUser.getDetails().getSports()) {
			System.out.print(sport.getName() + " ");
		}
		
		//Get a list of sports that are not acceptable
		System.out.print("\nEnter the sports which you should not do with this condition (space separated): ");
		String bannedSportsString = input.nextLine();
		ArrayList<Sport> bannedSports = new ArrayList<Sport>();
		
		for(Sport sport: currentUser.getDetails().getSports()) {	
			for(String sportString: bannedSportsString.split(" ")) {
				if(sport.getName().equalsIgnoreCase(sportString)) {
					bannedSports.add(sport);
					break;
				}
			}
		}
		
		//Illness or Injury
		System.out.print("Is the condition an illness (y for illness/n for injury)? ");
		String response = input.nextLine().trim();
		boolean illness = false;
		
		if(response.toLowerCase().equals("y")) {
			illness = true;
		}
		
		//Get the next Id from AccountDetails
		long conditionId = currentUser.getDetails().giveNextConditionId();
		
		//Create the HealthCondition object
		HealthCondition toAdd = new HealthCondition(name, date, illness, conditionId);
		
		//Add the bannedSports
		for(Sport sport: bannedSports) {
			toAdd.addBannedSport(sport);
		}
		
		//Add the HealthCondition to the user.
		currentUser.addCondition(toAdd);
		
	}

}
