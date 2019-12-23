import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class for the project. This class reads a user in from
 * a user file, and also writes a user to a user file. It also responds
 * to requests from clients and has a commandline interface. 
 * @author Andrew
 *
 */
public class TrainingPlanner {
	
	private ArrayList<User> users;
	
	/*
	 * A main function to create a system and start the interface.
	 */
	public static void main(String[] args) {
		
		//Create a training planner object to work with
		TrainingPlanner system = new TrainingPlanner();
		
		//Run the commandline interface
		system.runCMD(args);

	}

	/*
	 * Reads in a user and provides a commandline interface to access it.
	 * The user to be read is provided as the first command line argument.
	 * If no user is provided then a new user can be created.
	 */
	private void runCMD(String[] args) {
		
		//Read in the current user named in the first command line argument
		//User currentUser = readUser(args[0]);
		
		//Set up a scanner to get input from CMD
		Scanner input = new Scanner(System.in);
		
		boolean shutdown = false;
		
		//The command line options
		while(!shutdown) {
		
			//Print prompt
			System.out.print(args[0] + ">>");
			//Read the next command
			String nextCommand = input.next().trim();
			
			if(nextCommand.equals("show-user")) {
				//Show user details
				System.out.println("Command received: " + nextCommand);
			
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
				System.out.println("Are you sure you wish to exit? (Y/N)");
				String response = input.next().trim();
				
				if(response.toLowerCase().equals("y")) {
					shutdown = true;
				}
				
			} else {
				
				//Command not recognised
				System.out.println("Command not recognised. Type 'help' to see list"
						+ " of commands or 'exit' to exit.");
				
			}
			
		}
		
	}

	private User readUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
