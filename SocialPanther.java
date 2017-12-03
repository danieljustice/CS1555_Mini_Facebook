//Jordan Carr and Daniel Justice
//Main database driver
//CS1555

import java.uti.*;
import java.sql.*;

public class SocialPanther
{
	Database db;
	public static void main(String args[])
	{
		prompt_login();
	}

	private void prompt_login()
	{
		System.out.println("Welcome to the SocialPanther social network!");
		System.out.println("Enter your username login to Oracle: ");

		Scanner scan = new Scanner(System.in);
		String oUsername = scan.nextLine();

		System.out.println("Enter your password to login to Oracle: ");
		String oPass = scan.nextLine();

		db = new Database(oUsername, oPass);

		//Prompt either login or new account creation
		String input = null;
		do{
			System.out.println("Press 1 to login or 0 to create a new account.");
			input = scan.nextLine();
		}while(!input.equals("1") && !input.equals("0"));

		if(input.equals("1"))
		{
			System.out.println("Enter your SocialPanther username: ");
			String username = scan.nextLine();

			System.out.println("Enter your SocialPanther password: ");
			String pass = scan.nextLine();

			while(!db.loginUser(username, pass))
			{
				System.out.println("Username or password was incorrect.");

				System.out.println("Enter your SocialPanther username: ");
				username = scan.nextLine();

				System.out.println("Enter your SocialPanther password: ");
				pass = scan.nextLine();
			}
		}
		else
		{
			//Create a new user and automatically login the user
			prompt_createUser();
		}
	}

	//Creates a new user and automatically logs the user in on this account
	private void prompt_createUser()
	{
		//Get user's name
		String name = null;
		do
		{
			System.out.println("Enter your name: ");
			name = scan.nextLine();
		}while(name.length() > 20 || name.length() == 0);

		//Get user's userID
		String userID = null;
		do
		{
			System.out.println("Enter your userID: ");
			userID = scan.nextLine();
		}while(userID.length() > 20 || userID.length() == 0);

		//Get user's email
		String email = null;
		do
		{
			System.out.println("Enter your email: ");
			email = scan.nextLine();
		}while(email.length() > 20 || email.length() == 0);

		//Get the user's password
		String password = null;
		do
		{
			System.out.println("Enter your password: ");
			password = scan.nextLine();
		}while(password.length() > 20 || password.length() == 0);

		//Get the user's date of birth
		String dob = null;
		//Need to work on this

		//Create to new account
		db.createUser(userID, name, password, dob, email);
		db.loginUser(userID, password);
	}
}