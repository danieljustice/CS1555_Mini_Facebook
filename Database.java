import java.sql.*;
import java.util.*;

public class Database
{
	private Connection dbcon;
	private Timestamp last_login;

	//Client must ask for user and password themselves
	public Database(String username, String password) throws SQLException
	{
		//Open the connection
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
		dbcon = DriverManager.getConnection(url, username, password);
	}

	//Implement the following functions

	//Given a name, email address, and date of birth, add a new user to the system by inserting as
	//new entry in the profile relation.
	public void createUser(String userID, String name, String password, String dob, String email)
	{	
		//Add something to convert the date to a timestamp
		try
		{
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO profile values(?, ?, ?, ?, NULL, ?)");
			st1.setString(1, userID);
			st1.setString(2, name);
			st1.setString(3, password);
			st1.setString(4, dob);
			st1.setString(5, email);
			st1.executeUpdate();
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	//Given userID and password, login as the user in the system when an appropriate match is
	//found
	//Return true if logged in successfully, false otherwise
	public boolean loginUser(String userID, String password)
	{
		try
		{
			//Get the password given the userID
			PreparedStatement st1 = dbcon.prepareStatement("SELECT password FROM profile WHERE userID = ?");
			st1.setString(1, userID);
			ResultSet  result = st1.executeQuery();
			result.next();
			String pw = result.getString("password");

			//System.out.println("input: " + password + "\nresult: " + pw);

			//Compare the password with what's given
			if(password.equals(pw))
			{
				//Save the last login time for future use
				PreparedStatement st3 = dbcon.prepareStatement("SELECT lastlogin FROM profile WHERE userID = ?");
				st3.setString(1, userID);
				ResultSet res = st3.executeQuery();
				result.next();
				last_login = res.getTimestamp(lastlogin);

				//Fix so it can add proper date
				/*PreparedStatement st2 = dbcon.prepareStatement("UPDATE profile SET lastlogin = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') WHERE userID = ?");
				st2.setTimestamp(1, new java.util.Date());
				st2.setString(2, userID);
				st2.executeUpdate();*/
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
			return false;
		}

	}

	//Create a pending friendship from the (logged in) user to another user based on userID. The
	//application should display the name of the person that will be sent a friends request and the user
	//should be prompted to enter a message to be sent along with the request. A last confirmation
	//should be requested of the user before an entry is inserted into the pendingFriends relation,
	//and success or failure feedback is displayed for the user.
	public boolean initiateFriendship(String fromID, String toID)
	{
		//Get a message from the user
		System.out.println("Sending a request to " + toID);
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a message for your friendrequest:");
		String msg = scan.nextLine();

		//Get the name of the person to request to

		//Confirm request
		System.out.println("Are you sure you want to send a request to " + toID + "?");
		System.out.println("Enter yes or no:");
		String response = scan.nextLine();
		while(!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no"))
		{
			System.out.println("Enter yes or no:");
			response = scan.nextLine();
		}

		//Need to add error catching
		if(response.equalsIgnoreCase("yes"))
		{
			try
			{
				PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO pendingFriends values(?, ?, ?)");
				st1.setString(1, fromID);
				st1.setString(2, toID);
				st1.setString(3, msg);
				st1.executeUpdate();
				return true;
			}
			catch(SQLException e1)
			{
				//Print errors
				System.out.println("SQL Error");
				while(e1 != null)
				{
					System.out.println("Message = "+ e1.getMessage());
					System.out.println("SQLState = "+ e1.getSQLState());
					System.out.println("SQLState = "+ e1.getErrorCode());
					e1 = e1.getNextException();
				}
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	//This task should first display a formatted, numbered list of all outstanding friends and group
	//requests with an associated messages. Then, the user should be prompted for a number of the
	//request he or she would like to confirm or given the option to confirm them all. The application
	//should move the request from the appropriate pendingFriends or pendingGroupmembers
	//relation to the friends or groupMembership relation. The remaining requests which were not
	//selected are declined and removed from pendingFriends and pendingGroupmembers relations.
	public void confirmFriendship(String userID)
	{
		try
		{
			//Search for friend requests to confirm
			PreparedStatement st1 = dbcon.prepareStatement("SELECT fromID, message FROM pendingFriends WHERE toID = ?");
			st1.setString(1, userID);
			ResultSet friends = dbcon.executeQuery();

			//Search for group requests to confirm
			//Display the results
			//Ask which requests to confirm
			//Move those requests to the appropriate place
			//Delete the others
		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}

	}

	/*This task supports the browsing of the user's friends and of their friends' profiles. It first
	displays each of the user's friends' names and userIDs and those of any friend of those friends.
	Then it allows the user to either retrieve a friend's entire profile by entering the appropriate
	userID or exit browsing and return to the main menu by entering 0 as a userID. When selected,
	a friend's profile should be displayed in a nicely formatted way, after which the user should be
	prompted to either select to retrieve another friend's profile or return to the main menu.*/
	public void displayFriends()
	{
		try
		{

		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*Given a name, description, and membership limit, add a new group to the system, add the
	user as its first member with the role manager.*/
	public void createGroup(String name, String desc, int limit, String userID)
	{
		try
		{
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO groups values(?, ?, ?)");
			PreparedStatement st2 = dbcon.prepareStatement("INSERT INTO groupMembership values(?, ?, 'manager')");

			//Not sure how to determine gID
			st1.setString(2, name);
			st1.setString(3, desc);
			st1.executeUpdate();

			//Add the user to the group as a manager
			st2.setString(2, userID);
			st2.executeUpdate();
		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*Given a user and a group, create a pending request of adding to group (if not violate the
	group's membership limit). The user should be prompted to enter a message to be sent along
	with the request and inserted in the pendingGroupmembers relation.*/
	public void initiateAddingGroup(String userID, String gID)
	{
		try
		{
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter a message for your group request:");
			String msg = scan.nextLine();

			//Add the request to the database
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO pendingGroupmembers values(?, ?, ?)");
			st1.setString(1, gID);
			st1.setString(2, userID);
			st1.setString(3, msg);
			st1.executeUpdate();
		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*With this the user can send a message to one friend given his userID. The application should
	display the name of the recipient and the user should be prompted to enter the text of the
	message, which could be multi-lined. Once entered, the message should be 'sent' to the user
	by adding appropriate entries into the messages and messageRecipients relations by creating
	a trigger. The user should lastly be shown success or failure feedback.*/
	public void sendMessageToUser(String toID, String fromID)
	{
		//Prompt the user for a message to send
		//Change so it can be multilined
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your message: ");
		String msg = scan.nextLine();

		try
		{
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO messages values(?, ?, ?, ?, NULL, ?)");
			//Not sure how to determine message ID
			st1.setString(2, fromID);
			st1.setString(3, msg);
			st1.setString(4, toID);
			//Not sure of how to get the current date
			st1.executeUpdate();

			System.out.println("Sent successfully");
		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("Error: message failed to send");
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*With this the user can send a message to a recipient group, if the user is within the group.
	Every member of this group should receive the message. The user should be prompted to enter
	the text of the message, which could be multi-lined. Then the created new message should
	be 'sent' to the user by adding appropriate entries into the messages and messageRecipients
	relations by creating a trigger. The user should lastly be shown success or failure feedback.
	Note that if the user sends a message to one friend, you only need to put the friend's userID
	to ToUserID in the table of messages. If the user wants to send a message to a group, you need
	to put the group ID to ToGroupID in the table of messages and use a trigger to populate the
	messageRecipient table with proper user ID information as defined by the groupMembership
	relation.*/
	public void sendMessageToGroup()
	{
		try
		{

		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*When the user selects this option, the entire contents of every message sent to the user should
	be displayed in a nicely formatted way.*/
	public void displayMessages(String userID)
	{
		try
		{
			//Get all the messages sent to the user and order by dateSent descending
			PreparedStatement st1 = dbcon.prepareStatement("SELECT fromID, message, dateSent FROM messages WHERE toUserID = ? ORDER BY dateSent DESC");
			st1.setString(1, userID);
			ResultSet results = st1.executeQuery();

			//Loop through and display the results
			while(results.next())
			{
				System.out.println("From " + results.getString("fromID"));
				System.out.println("Sent " + results.getDate("dateSent"));
				System.out.println(results.getString("message"));
			}
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*This should display messages in the same fashion as the previous task except that only those
	messages sent since the last time the user logged into the system should be displayed.*/
	public void displayNewMessages(String userID)
	{
		try
		{
			//Get all messages sent to the user since last login time
			PreparedStatement st1 = dbcon.prepareStatement("SELECT fromID, message, dateSent FROM messages WHERE toUserID = ? and dateSent > ? ORDER BY dateSent DESC");
			st1.setString(1, userID);
			st2.setTimestamp(2, last_login);
			ResultSet results = st1.executeQuery();

			//Loop through and display results
			while(results.next())
			{
				System.out.println("From " + results.getString("fromID"));
				System.out.println("Sent " + results.getDate("dateSent"));
				System.out.println(results.getString("message"));
			}
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*Given a string on which to match any user in the system, any item in this string must be
	matched against any significant field of a user's profile. That is if the user searches for 'xyz
	abc', the results should be the set of all profiles that match 'xyz' union the set of all profiles
	that matches 'abc'*/
	public void searchForUser()
	{
		try
		{

		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*Given two users (A and B), find a path, if one exists, between A and B with at most 3 hop
	between them. A hop is defined as a friendship between any two users.*/
	public void threeDegrees()
	{
		try
		{

		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*Display top K who have sent to received the highest number of messages during for the past x
	months. x and K are input parameters to this function.*/
	public void topMessages(int k, int months)
	{
		try
		{

		}
		catch(SQLException e1)
		{
			//Print errors
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*Remove a user and all of their information from the system. When a user is removed, the system
	should delete the user from the groups he or she was a member of using a trigger. Note:
	messages require special handling because they are owned by both sender and receiver. Therefore,
	a message is deleted only when both he sender and all receivers are deleted. Attention
	should be paid handling integrity constraints.*/
	public void dropUser(String userID)
	{
		try
		{
			//Have triggers handle the details of this
			PreparedStatement st1 = dbcon.prepareStatement("DELETE FROM profile WHERE userID = ?");
			st1.setString(1, userID);
			st1.executeUpdate();
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLState());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	/*This option should cleanly shut down and exit the program after marking the time of the user's
logout in the profile relation,*/
	public void Logout()
	{
		closeDB();
	}

	public void closeDB() throws SQLException
	{
		//Close the connection
		dbcon.close();
	}
}