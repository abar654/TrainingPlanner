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
	public static final String DATE_FORMAT = "dd MM yyyy";
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
		
		//Create a CommandLineInterface object to do all the user interaction
		CommandLineInterface cli = new CommandLineInterface(input, this);
		
		//Read in the current user named in the first command line argument
		//If no user is provided then create a new user
		User currentUser = null;
		
		if(args.length > 0) {
			
			//readUser should return null if user not found
			currentUser = readUser(args[0]);
			
		}
		
		if(currentUser == null) {
			
			//Create a new user
			currentUser = cli.createNewUser();
			
			//Add the username to the usernameSet
			usernameSet.add(currentUser.getDetails().getUsername());
			
			//Save the new User to a file
			writeUser(currentUser);
			
		}
		
		cli.run(currentUser);
		
	}

	public void writeUser(User user) {
		
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

	public boolean isValidUsername(String username) {
		boolean isValid = true;
		
		if(!username.matches("^(\\w){6,18}$") || usernameSet.contains(username)){
			isValid = false;
		}
		
		return isValid;
	}

	public User readUser(String username) {
		
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
