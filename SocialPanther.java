//Jordan Carr and Daniel Justice
//Main database driver
//CS1555

import java.util.*;
import java.sql.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SocialPanther
{
	private static Database db;
	private static boolean logged_in;
	private static Scanner scanner;
	private static InputStream gstream;

	public static void main(String args[]) throws SQLException
	{
		RunSocialPanther();
	}



	public static void RunSocialPanther() throws SQLException{
		RunSocialPanther(System.in);
	}

	public static void RunSocialPanther(InputStream inputStream) throws SQLException{
		scanner = new Scanner(inputStream);
		gstream = inputStream;

		if(!prompt_db_login()){
			return;
		}

		while(prompt_login() != -1){

			
			//Cycyle through the menu until the user logs out
			while(logged_in)
			{
				displayMenu();
				String input = scanner.nextLine();

				//Check the input for errors
				if(input.equals("0"))
				{
					db.Logout();
					logged_in = false;
				}
				else if(input.equals("1"))
					db.displayFriends();
				else if(input.equals("2"))
					db.confirmFriendship();
				else if(input.equals("3"))
					groupCreation();
				else if(input.equals("4"))
					friendRequest();
				else if(input.equals("5"))
					groupRequest();
				else if(input.equals("6"))
					userMessage();
				else if(input.equals("7"))
					groupMessage();
				else if(input.equals("8"))
					db.displayMessages();
				else if(input.equals("9"))
					db.displayNewMessages();
				else if(input.equals("10"))
					userSearch();
				else if(input.equals("11"))
					search3DegPath();
				else if(input.equals("12"))
					topK();
				else if(input.equals("13"))
				{
					System.out.println("SocialPanther is a social network pioneered by Daniel Justice and Jordan Carr.");
					System.out.println("It was created during the Fall of 2017 for a Database class (CS1555) at the University of Pittsburgh.");
					System.out.println("SocialPanther is run on Java with an Oracle based SQL database.");
					System.out.println("Feel free to explore what SocialPanther has to offer!");
				}
				else if(input.equals("14"))
				{
					db.dropUser();
					logged_in = false;
					System.out.println("It's a shame to see you go. We hope you will join again in the future!");
				}
				else
					System.out.println("Invalid Input\n");
			}
		}
		db.closeDB();
		System.out.println("See you again soon!");
	}

	//Display contents of the main menu
	private static void displayMenu()
	{
		System.out.println("Welcome to SocialPanther! \nSelect one of the following to do something:");
		System.out.println("0.\tLog out");
		System.out.println("1.\tDisplay Friends");
		System.out.println("2.\tConfirm Pending Requests");
		System.out.println("3.\tCreate a New Group");
		System.out.println("4.\tSend a Friend Request To Another User");
		System.out.println("5.\tSend a Request To a Group");
		System.out.println("6.\tSend a Message To a Friend");
		System.out.println("7.\tSend a Message To a Group");
		System.out.println("8.\tDisplay All Messages");
		System.out.println("9.\tDispaly All New Messages");
		System.out.println("10.\tSearch For a User");
		System.out.println("11.\tSerach For a Third Degree Path");
		System.out.println("12.\tDisplay The Top Messaged Users in the Past Few Months");
		System.out.println("13.\tAbout SocialPanther");
		System.out.println("14.\tDelete Your Profile");
	}

	//Prompt the user to enter the top K messaged users in the past x months
	private static void topK() throws SQLException
	{
		try
		{
			System.out.println("Enter how many users do you want in your results:");
			int k = scanner.nextInt();
			System.out.println("Enter how many months in the past you want your results to go back:");
			int x = scanner.nextInt();
			db.topMessages(k, x);
		}
		catch(InputMismatchException e4)
		{
			System.out.println("Invalid Input: you must enter integers as input");
		}
	}

	//Prompt the user to enter two userID's to find a path between
	private static void search3DegPath() throws SQLException
	{

		System.out.println("Enter the first userID (the starting point):");
		String userID1 = scanner.nextLine();
		System.out.println("Enter the second userID (the end point):");
		String userID2 = scanner.nextLine();

		db.threeDegrees(userID1, userID2);
	}

	//Prompt the user to search for another user
	private static void userSearch() throws SQLException
	{
		System.out.println("Enter the things you would like to search for:");
		db.searchForUser(scanner.nextLine());
	}

	//Ask the user which group to send the message to
	private static void groupMessage() throws SQLException
	{
		System.out.println("Enter the group ID of the group you would like to send a message to:");
		db.sendMessageToGroup(scanner.nextLine());
	}

	//Ask the user who he/she would like to send a message to
	private static void userMessage() throws SQLException
	{
		System.out.println("Enter the userID of the friend you would like to send a message to:");
		db.sendMessageToUser(scanner.nextLine());
	}

	//Prompt the user to enter the ID of a group to request
	private static void groupRequest() throws SQLException
	{
		System.out.println("Enter the group ID of the group you would like to send a request to:");

		db.initiateAddingGroup(scanner.nextLine());
	}

	//Prompt a user to enter an ID for a friend request
	private static void friendRequest() throws SQLException
	{
		System.out.println("Enter the userID of the friend you would like to send a request to:");
		if(gstream == System.in){
			db.initiateFriendship(scanner.nextLine());
		}else{
			db.initiateFriendship(scanner.nextLine(), scanner.nextLine());
		}
		
	}

	//Prompt user for information regarding group creation and then create it
	private static void groupCreation() throws SQLException
	{
		System.out.println("Enter the name of your group:");
		String name = scanner.nextLine();
		System.out.println("Enter a brief description of your group:");
		String des = scanner.nextLine();
		
		//Check that the input is an integer
		boolean valid = false;
		int limit = 0;
		while(!valid)
		{
			try
			{
				System.out.println("Enter the maximum number of members allowed in your group:");
				limit = scanner.nextInt();
				valid = true;
			}
			catch(InputMismatchException e1)
			{
				System.out.println("Invalid Input: Please enter an integer");
			}
		}

		//Create the group
		db.createGroup(name, des, limit);
	}

	private static boolean prompt_db_login() throws SQLException{
		//Prompt login to Oracle
		System.out.println("Welcome to the SocialPanther social network!");
		System.out.println("Enter your username login to Oracle: ");

		String oUsername = scanner.nextLine();

		System.out.println("Enter your password to login to Oracle: ");
		String oPass = scanner.nextLine();

		db = new Database(oUsername, oPass);

		return db != null;
	}

	private static void log_in_user(){

		System.out.println("Enter your SocialPanther userID: ");
		String username = scanner.nextLine();

		System.out.println("Enter your SocialPanther password: ");
		String pass = scanner.nextLine();

		logged_in = db.loginUser(username, pass);
		if(!logged_in){
			System.out.println("Username or password was incorrect.");
		}
	}

	//Prompt the user to login
	private static int prompt_login() throws SQLException
	{
		
		//Prompt either login or new account creation
		String input = null;
		do{
			System.out.println("Press 1 to login, 0 to create a new account or -1 to quit.");
			input = scanner.nextLine();
		}while(!input.equals("1") && !input.equals("0") && !input.equals("-1"));

		//Log in the user
		if(input.equals("1"))
		{
			log_in_user();
		}
		else if(input.equals("0"))
		{
			//Create a new user and automatically login the user
			prompt_createUser();
		}

		if(!logged_in && !input.equals("-1")){
			prompt_login();
		}

		return Integer.parseInt(input);
	}

	//Creates a new user and automatically logs the user in on this account
	//Note- error checks for date of birth are not working properly (infinite loop)
	private static void prompt_createUser() throws SQLException
	{
		boolean success = false;
		String userID;
		String password;
		// do
		// {
			//Get user's name
			String name = null;
			do
			{
				System.out.println("Enter your name: ");
				name = scanner.nextLine();
			}while(name.length() > 20 || name.length() == 0);

			//Get user's userID
			userID = null;
			do
			{
				System.out.println("Enter your userID: ");
				userID = scanner.nextLine();
			}while(userID.length() > 20 || userID.length() == 0);

			//Get user's email
			String email = null;
			do
			{
				System.out.println("Enter your email: ");
				email = scanner.nextLine();
			}while(email.length() > 20 || email.length() == 0);

			//Get the user's password
			password = null;
			do
			{
				System.out.println("Enter your password: ");
				password = scanner.nextLine();
			}while(password.length() > 20 || password.length() == 0);

			//Get the user's date of birth
			//String dob = null;
			
			//Ask for the month of birth
			boolean valid = false;
			int month = 0;
			while(!valid)
			{
				try
				{
					System.out.println("Enter your month of birth as an integer:");
					month = scanner.nextInt();
					scanner.nextLine();
					//Do an error check on the month
					if(month > 12 || month < 1)
					{
						System.out.println("Invalid Input: Please enter an integer between 1 and 12 for your month of birth");
					}
					else
						valid = true;
				}
				catch(InputMismatchException e1)
				{
					System.out.println("Invalid Input: Please enter an integer");
					scanner.next();
					continue;
				}
			}

			//Ask for the day of birth
			valid = false;
			int day = 0;
			while(!valid)
			{
				try
				{
					System.out.println("Enter your day of birth: ");
					day = scanner.nextInt();
					scanner.nextLine();
					//Do an error check on the day
					if((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && (day < 1 || day > 31))
					{
						System.out.println("Invalid Input: Please enter a value between 1 and 31");
					}
					else if((month == 4 || month == 6 || month == 9 || month == 11) && (day < 1 || day > 30))
					{
						System.out.println("Invalid Input: Please enter a value between 1 and 30");
					}
					else if(month == 2 && (day < 1 || day > 29))
					{
						System.out.println("Invalid Input: Please enter a value between 1 and 29");
					}
					else
					{
						valid = true;
					}
				}
				catch(InputMismatchException e2)
				{
					System.out.println("Invalid Input: Please enter an integer");
					scanner.next();
					continue;
				}
			}

			//Ask for the year of birth
			valid = false;
			int year = 0;
			while(!valid)
			{
				try
				{
					System.out.println("Enter your year of birth:");
					year = scanner.nextInt();
					scanner.nextLine();
					valid = true;
				}
				catch(InputMismatchException e3)
				{
					System.out.println("Invalid Input: Please enter an integer");
					scanner.next();
					continue;
				}
			}

			java.sql.Date dob = new java.sql.Date(year, month, day);

			//Create to new account
			success = db.createUser(userID, name, password, dob, email);
		// }while(!success);
		if(success){
			logged_in = db.loginUser(userID, password);
		}
	
	}
}