import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * The main class for the project. This class reads a user in from
 * a user file, and also writes a user to a user file. It also responds
 * to requests from clients and has a commandline interface. 
 * @author Andrew
 *
 */
public class TrainingPlanner {
	
	private HashSet<String> usernameSet;
	private static final String DATE_FORMAT = "dd MM yyyy";
	private static final String USER_DIR = "users";
	private static final String USER_EXT = ".tpu";
	
	/*
	 * A main function to create a system and start the interface.
	 */
	public static void main(String[] args) {
		
		//Create a training planner object to work with
		TrainingPlanner system = new TrainingPlanner();
		
		//Name of testfile if any
		//Set to null to input via commandline directly
		//String testfile = "test2-weekdata";
		String testfile = null;
		
		//Run the commandline interface
		system.runCMD(args, testfile);

	}
	
	public TrainingPlanner() {
		
		usernameSet = new HashSet<String>();
		
		//Read through the names of all the files in the users dir to get a set of usernames
		File userDir = new File(USER_DIR);
		File[] userFiles = userDir.listFiles();
		
		for(int i = 0; i < userFiles.length; i++) {
			
			if(userFiles[i].isFile()) {
				
				String filename = userFiles[i].getName();
				String ext = filename.substring(filename.length()-USER_EXT.length(), filename.length());
				String nextUsername = filename.substring(0, filename.length()-USER_EXT.length());
				
				//Check the file extension, then add to HashSet
				if(ext.equals(USER_EXT)) {
					usernameSet.add(nextUsername);
				}
				
			}
			
		}
		
	}

	/*
	 * Reads in a user and provides a commandline interface to access it.
	 * The user to be read is provided as the first command line argument.
	 * If no user is provided then a new user can be created.
	 */
	private void runCMD(String[] args, String testfile) {
		
		//Set up the input
		Scanner input;
		
		if(testfile != null) {
			
			//Set up the scanner to get input from test file
			File inputFile = new File(testfile);
			try {
				input = new Scanner(inputFile);
			} catch(FileNotFoundException e) {
				input = new Scanner(System.in);
			}
			
		} else {
		
			//Set up a scanner to get input from CMD
			input = new Scanner(System.in);
			
		}
		
		//Set the focus date to right now
		LocalDate focusDate = LocalDate.now();
		
		//Read in the current user named in the first command line argument
		//If no user is provided then create a new user
		User currentUser = null;
		
		if(args.length > 0) {
			
			//readUser should return null if user not found
			currentUser = readUser(args[0]);
			
		}
		
		if(currentUser == null) {
			
			System.out.println("Valid username not provided - create a new user");
			
			//Collect the required information for a new user
			//Get the name
			System.out.print("Enter a name: ");
			String name = input.nextLine().trim();
			
			//Get the date of birth
			System.out.print("Enter your date of birth (" + DATE_FORMAT + "): ");
			boolean dateSuccess = false;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
			LocalDate date = null;
			
			while(!dateSuccess) {
				
				String text = input.nextLine().trim();
				
				try {
					date = LocalDate.parse(text, formatter);
					dateSuccess = true;
				} catch (DateTimeParseException e) {
					System.out.print("Date error. Please enter date in format (" + DATE_FORMAT + "): ");
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
				
				if(isValidUsername(username)) {
					usernameSuccess = true;
					usernameSet.add(username);
				} else {
					System.out.println("Username unavailable. Make sure to use only letters, numbers or underscores (6 to 18 characters).");
					System.out.print("Please enter another username: ");
				}
				
			}
			
			//Create the new User's AccountDetails object
			AccountDetails userDetails = new AccountDetails(username, name, date, email, sport);
			
			//Create the new User
			currentUser = new User(userDetails);
			
			//Save the new User to a file
			writeUser(currentUser);
			
		}
		
		boolean shutdown = false;
		
		//The command line options
		while(!shutdown) {
		
			//Print prompt
			System.out.print(currentUser.getDetails().getName() + ">>");
			//Read the next command
			String nextCommand = input.nextLine().trim();
			
			if(nextCommand.equals("show-user")) {
				
				//Show user details
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				System.out.println(gson.toJson(currentUser.getDetails()));
			
			} else if(nextCommand.equals("add-session")) {
				
				//Add a session
				
				//Get all the information to create the session
				
				//Get the date
				System.out.print("Date for this session: ");
				boolean dateSuccess = false;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
				LocalDate date = null;
				
				while(!dateSuccess) {
					
					String text = input.nextLine().trim();
					
					try {
						date = LocalDate.parse(text, formatter);
						dateSuccess = true;
					} catch (DateTimeParseException e) {
						System.out.print("Date error. Please enter date in format (" + DATE_FORMAT + "): ");
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
				
			} else if(nextCommand.equals("add-condition")) {
				
				//Add a condition
				//To be implemented in Stage 3
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("remove-session")) {
				
				//Remove a session
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
				
			} else if(nextCommand.equals("remove-condition")) {
				
				//Remove a condition
				//To be implemented in Stage 3
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("set-date")) {
				
				//Set focus date
				System.out.print("Enter a date during the week you would like to show: ");
				boolean dateSuccess = false;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
				
				while(!dateSuccess) {
					
					String text = input.nextLine().trim();
					
					try {
						focusDate = LocalDate.parse(text, formatter);
						dateSuccess = true;
					} catch (DateTimeParseException e) {
						System.out.print("Date error. Please enter date in format (" + DATE_FORMAT + "): ");
					}
					
				}
				
			} else if(nextCommand.equals("show-sessions")) {
				
				//Show current training week sessions
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				System.out.println(gson.toJson(currentUser.getTrainingWeek(focusDate)));
				
			} else if(nextCommand.equals("show-data")) {
				
				//Show current training week data
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

				
			} else if(nextCommand.equals("show-conditions")) {
				
				//Show conditions for current training week
				//To be implemented in stage 3
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("show-recommendations")) {
				
				//Show recommendations based on current training week
				//To be implemented in stage 4
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("save")) {
				
				//Saves current user state to file.
				writeUser(currentUser);
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
								+ "remove-session: Remove a session\n"
								+ "remove-condition: Remove a condition\n"
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

	private void writeUser(User user) {
		
		//Open for writing the file in the users directory which has the name of the user
		try {
			
			FileWriter outputFile = new FileWriter(USER_DIR + "/" + user.getDetails().getUsername() + USER_EXT);
			BufferedWriter output = new BufferedWriter(outputFile);
			
			//Create a GSON object to serialise
			//NOTE: The Condition class (part of HealthState) will need a custom serialiser
			//as it contains references to Sessions and we only want to store session IDs not the whole objects again.
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			output.write(gson.toJson(user));
			
			output.close();
			outputFile.close();
			
		} catch(Exception e) {
			System.out.println("Error writing file: " + e);
		}
		
	}

	private boolean isValidUsername(String username) {
		boolean isValid = true;
		
		if(!username.matches("^(\\w){6,18}$") || usernameSet.contains(username)){
			isValid = false;
		}
		
		return isValid;
	}

	private User readUser(String username) {
		
		try {
			
			//Open the user's file
			File userfile = new File(USER_DIR + "/" + username + USER_EXT);
			FileReader userReader = new FileReader(userfile);
			
			//Give the input stream to the JSON parser
			Gson gson = new Gson();
			JsonReader jsonReader = new JsonReader(userReader);
			
			//Read the JSON file into a JSON element
			JsonObject userJson = (JsonObject) JsonParser.parseReader(jsonReader);

			//Build the User object
			User user = gson.fromJson(userJson, User.class);
			
			return user;
			
		} catch(FileNotFoundException e) {
			
			return null;
						
		}
		
	}

}
