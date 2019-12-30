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
			Sport sport = new Sport(input.nextLine().trim(), 0);
			
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
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("add-condition")) {
				
				//Add a condition
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("remove-session")) {
				
				//Remove a session
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("remove-condition")) {
				
				//Remove a condition
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("set-date")) {
				
				//Set focus date
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("show-sessions")) {
				
				//Show current training week sessions
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("show-data")) {
				
				//Show current training week data
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("show-conditions")) {
				
				//Show conditions for current training week
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("show-recommendations")) {
				
				//Show recommendations based on current training week
				System.out.println("Command received: " + nextCommand);
				
			} else if(nextCommand.equals("save")) {
				
				//Saves current user state to file.
				System.out.println("Command received: " + nextCommand);
				
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
