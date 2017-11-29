//Test Driver program for Database.java

import java.util.*;
import java.io.ByteArrayInputStream;
import java.sql.*;

public class TestDriver
{
	public static Database db;
	public static void main(String args[]) throws SQLException
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your Oracle username:");
		String username = scan.nextLine();
		System.out.println("Enter your Oracle password:");
		String password = scan.nextLine();
		
		try
		{
			db = new Database(username, password);
		}
		catch(SQLException e1)
		{
			System.out.println("Error when logging in");
			System.exit(0);
		}

		runTestSuite();

		db.closeDB();
	}

	public static void runTestSuite(){
		testCreatingUser();
		// testLoginUser();
		// testInitiateFriendship();
		testDropUser();
	}

	public static void testCreatingUser(){
		// db.dropUser("1");
		// System.out.println("Deleted User");
		//Test user creation
		int success = db.createUser("695", "John", "slfslj", "5-May-1987", "yy220@pitt.edu");
		if(success < 0){
			System.out.println("testCreatingUser - FAILED");
		}else{
			System.out.println("testCreatingUser - Passed");
		}
	}

	public static void testLoginUser(){
		//Test login
		if(db.loginUser("695", "slfslj"))
			System.out.println("testLoginUser - Passed");
		else
			System.out.println("testLoginUser - FAILED");
	}

	public static void testInitiateFriendship(){
		ByteArrayInputStream in  =  new ByteArrayInputStream("My string\nyes\n".getBytes());
		boolean success = db.initiateFriendship("1", "695", in);
		if(success){
			System.out.println("testInitiateFriendship - Passed");
		}else{
			System.out.println("testInitiateFriendship - FAILED");
		}
	}

	public static void testConfirmFriendship(){

	}
	public static void testDisplayFriends(){

	}

	public static void testDropUser(){
		System.out.println("Starting drop user.");
		db.dropUser(695);
		System.out.println("FINISHED drop user.");
	}
	
}