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
		flashDB();
		testCreatingUser();
		testDropUser();
		testLoginUser();
		testInitiateFriendship();
		
	}

	//super dependent on drops working properly, horrible dependency issues
	public static void flashDB(){
		db.dropUser(601);
		db.dropUser(602);
		db.dropUser(603);
		db.dropUser(604);
		db.dropUser(605);
	}

	public static void testCreatingUser(){
		// db.dropUser("1");
		// System.out.println("Deleted User");
		//drop user
		db.dropUser(601);
		//Test user creation
		int success = db.createUser("601", "John", "slfslj", "5-May-1987", "yy220@pitt.edu");
		if(success < 0){
			System.out.println("testCreatingUser - FAILED");
		}else{
			System.out.println("testCreatingUser - Passed");
		}
	}

	public static void testLoginUser(){
		//drop created users 
		db.dropUser(603);
		//Test login
		db.createUser("603", "John", "slfslj", "5-May-1987", "yy220@pitt.edu");
		if(db.loginUser("603", "slfslj"))
			System.out.println("testLoginUser - Passed");
		else
			System.out.println("testLoginUser - FAILED");
	}

	public static void testInitiateFriendship(){
		//drop created users
		db.dropUser(604);
		db.dropUser(605);
		//fake input from the user
		ByteArrayInputStream in  =  new ByteArrayInputStream("My string\nyes\n".getBytes());
		
		db.createUser("604", "John", "slfslj", "5-May-1987", "yy220@pitt.edu");
		db.createUser("605", "John", "slfslj", "5-May-1987", "yy220@pitt.edu");
		
		boolean success = db.initiateFriendship("604", "605", in);
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
		//drop user, this is an odd dependency....
		db.dropUser(602);

		db.createUser("602", "John", "slfslj", "5-May-1987", "yy220@pitt.edu");
		if(db.dropUser(602) == 1){
			System.out.println("testDropUser - Passed");
		}else{
			System.out.println("testDropUser - FAILED");
		}
	}
	
}