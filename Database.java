import java.sql.*;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Database
{
	/*Note: there are plans of instead of passing the same userID to the system everytime 
	*it will be saved as a class variable and will be set to accordingly on Login and
	*set to null on logout.
	*This will be implemented in the final version of the project.
	*/
	private Connection dbcon;
	private Timestamp last_login;
	private String thisUserID;

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
	public boolean createUser(String userID, String name, String password, java.sql.Date dob, String email)
	{	
		//Add something to convert the date to a timestamp
		//success will be returned at end of method, -1 for fail, 0 or greater for success
		//int success = -1;
		try
		{
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO profile values(?, ?, ?, ?, NULL, ?)");
			st1.setString(1, userID);
			st1.setString(2, name);
			st1.setString(3, password);
			st1.setDate(4, dob);
			st1.setString(5, email);
			st1.executeUpdate();
			return true;
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error in createUser method");
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
				thisUserID = userID;
				ResultSet res = st3.executeQuery();
				res.next();
				last_login = res.getTimestamp("lastlogin");

				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error in loginUser method");
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

	public boolean initiateFriendship(String toID)
	{
		//Using method overload here to have a version of this method that
		//can be easily tested
		return initiateFriendship(thisUserID, toID, System.in);
	}

	//Allows us to feed an inputstream into this method so that we can automate the tests
	public boolean initiateFriendship(String fromID, String toID, InputStream in)
	{
		//Get a message from the user
		System.out.println("Sending a request to " + toID);
		Scanner scan = new Scanner(in);
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
				System.out.println("SQL Error in initiateFriendship");
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
	public void confirmFriendship(){
		confirmFriendship(thisUserID, System.in);
	}
	//This task should first display a formatted, numbered list of all outstanding friends and group
	//requests with an associated messages. Then, the user should be prompted for a number of the
	//request he or she would like to confirm or given the option to confirm them all. The application
	//should move the request from the appropriate pendingFriends or pendingGroupmembers
	//relation to the friends or groupMembership relation. The remaining requests which were not
	//selected are declined and removed from pendingFriends and pendingGroupmembers relations.
	public void confirmFriendship(String userID, InputStream in)
	{
		try
		{
			//Search for friend requests to confirm
			PreparedStatement st1 = dbcon.prepareStatement("SELECT fromID, message FROM pendingFriends WHERE toID = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT	);
			st1.setString(1, userID);
			ResultSet friends = st1.executeQuery();

			//Search for group requests to confirm
			PreparedStatement st2 = dbcon.prepareStatement("select pendingGroupmembers.userID, pendingGroupmembers.message, pendingGroupmembers.gID "
															+"from groupMembership join pendingGroupmembers "
																+"ON groupMembership.gID = pendingGroupmembers.gID "
															+"where ?=groupMembership.userID and 'manager'=groupMembership.role");
			st2.setString(1, userID);
			ResultSet groupFriends = st2.executeQuery();

			//Push friend results into a 2D arraylist
			List<List<String>> friendArr = new ArrayList<List<String>>();
			for(int i = 0; friends.next(); i++){
				friendArr.add(new ArrayList<String>());
				friendArr.get(i).add(friends.getString(1));
				friendArr.get(i).add(friends.getString(2));
				
			}
			//Push groupFriend results into a 2D arraylist
			List<List<String>> groupFriendsArr = new ArrayList<List<String>>();
			for(int i = 0; groupFriends.next(); i++){
				groupFriendsArr.add(new ArrayList<String>());
				groupFriendsArr.get(i).add(groupFriends.getString(1));
				groupFriendsArr.get(i).add(groupFriends.getString(2));
				groupFriendsArr.get(i).add(groupFriends.getString(3));
			}

			//Display pending friends
			int friendNum = 0;
			if(friendArr.size() > 0){
				System.out.println("Pending friends:\tTheir Messages:");
				for(friendNum = 0; friendNum < friendArr.size(); friendNum++){
					System.out.println(friendNum+1 + ")\t\t" + friendArr.get(friendNum).get(0) + "\t\t\t" + friendArr.get(friendNum).get(1));
				}
			}else{
				System.out.println("There are no pending friend requests for " + userID + ".");
			}

			//Display pending friends
			if(groupFriendsArr.size() > 0){
				System.out.println("Pending friends:\tGroupID:\tTheir Messages:");
				for(int i = 0; i < groupFriendsArr.size(); i++){
					System.out.println(friendNum+i+1 + ")\t\t" + groupFriendsArr.get(i).get(0) + "\t\t" + groupFriendsArr.get(i).get(2) + "\t\t\t" + groupFriendsArr.get(i).get(2));
				}
			}else{
				System.out.println("There are no pending friend requests for " + userID + "'s groups.");
			}
			
			//Ask which requests to confirm
			Scanner scan = new Scanner(in);
			String input = "";
			Set<Integer> friendsToAdd = new HashSet<Integer>();
			while(!input.contains("-1")){
				System.out.println();
				System.out.println("Type a profile's number to accept as friend.");
				System.out.println("Or type 0 to add all.");
				System.out.println("Or type -1 to quit and delete every request you do not accept.");
				input = scan.nextLine();
				//try to parse input and make sure it is a valid number
				try {
					int integerInput = Integer.parseInt(input);
					if(integerInput == 0){		//if add all, then add all and then set to exit
						//add all
						for(int i = 0; i < friendArr.size() + groupFriendsArr.size(); i++){
							friendsToAdd.add(i+1);
						}
						input = "-1";

					}else if(integerInput == -1){	//if -1, just let it go through and exit
						//do nothing
					}else if(integerInput> 0 && integerInput < friendArr.size() + groupFriendsArr.size() + 1){	//check range
						friendsToAdd.add(integerInput);
					}else{
						System.out.println("Not a valid input");
					}
				} catch (Exception e) {
					//TODO: handle exception
					System.out.println("Not a valid input");
				}
			}
			//INSERT INTO friends values(605, 604, CURRENT_DATE(), yo)
			//Move those requests to the appropriate place
			for(int index:friendsToAdd){
				if(index < friendArr.size()+1){
					PreparedStatement st3 = dbcon.prepareStatement("INSERT INTO friends values(?, ?, CURRENT_DATE, ?)");
					st3.setString(1, userID);
					st3.setString(2, friendArr.get(index-1).get(0));
					st3.setString(3, friendArr.get(index-1).get(1));
					st3.executeUpdate();

					friendArr.set(index-1, null);
				}else{
					//0 is the userID, 2 is the gID
					PreparedStatement st3 = dbcon.prepareStatement("INSERT INTO groupMembership values(?, ?, 'user')");
					st3.setString(1, groupFriendsArr.get(index-1).get(2));
					st3.setString(2, groupFriendsArr.get(index-1).get(0));
					st3.executeUpdate();
				}
			}
			//Delete the others
			for(int i = 0; i < friendArr.size(); i++){
				if(friendArr.get(i) != null){
					//drop pending request
					PreparedStatement st3 = dbcon.prepareStatement("DELETE FROM pendingFriends WHERE fromID = ? AND toID = ?");
					st3.setString(1, friendArr.get(i).get(0));
					st3.setString(2, userID);
					st3.executeQuery();
				}
			}

			for(int i = 0; i < groupFriendsArr.size(); i++){
				if(groupFriendsArr.get(i) != null){
					//drop pending request
					PreparedStatement st3 = dbcon.prepareStatement("DELETE FROM pendingGroupmembers WHERE gID = ? AND userID = ?");
					st3.setString(1, groupFriendsArr.get(i).get(2));
					st3.setString(2, groupFriendsArr.get(i).get(0));
					st3.executeQuery();
				}
			}
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
			//Get all the friends of the user
			PreparedStatement st1 = dbcon.prepareStatement("SELECT userID1, userID2, name FROM "
															+ "(friends JOIN profile ON (((userID1 = userID) and (userID1 <> ?)) or ((userID2 = userID) and (userID2 <> ?))))"
															+ "WHERE userID1 = ? OR userID2 = ?");
			PreparedStatement st2 = dbcon.prepareStatement("SELECT userID, name, date_of_birth, email FROM profile WHERE userID = ?");

			st1.setString(1, thisUserID);
			st1.setString(2, thisUserID);
			st1.setString(3, thisUserID);
			st1.setString(4, thisUserID);
			ResultSet friends = st1.executeQuery();

			//Display the friends
			System.out.println("Your friends:");
			ArrayList<String> list = new ArrayList<String>();
			while(friends.next())
			{
				if(thisUserID.equalsIgnoreCase(friends.getString("userID1")))
				{
					System.out.println("Name: " + friends.getString("name") + ", " + friends.getString("userID2"));
					list.add(friends.getString("userID2"));
					getFriends(friends.getString("userID2"), list);
				}
				else
				{
					System.out.println("Name: " + friends.getString("name") + ", " + friends.getString("userID1"));
					list.add(friends.getString("userID1"));
					getFriends(friends.getString("userID1"), list);
				}
			}

			//Menu to request profiles
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter a profileID to request a profile(enter 0 to exit):");
			String input = scan.nextLine();

			while(!input.equals("0"))
			{
				//Get the profile
				//User input error check
				if(list.contains(input))
				{
					st2.setString(1, input);
					ResultSet results = st2.executeQuery();
					results.next();

					//Display the results
					System.out.println("Name: " + results.getString("name"));
					System.out.println("userID: " + results.getString("userID"));
					System.out.println("email: " + results.getString("email"));
					System.out.println("Date of Birth:" + results.getDate("date_of_birth") + "\n");
				}
				//Ask for input
				System.out.println("Enter a profileID to request a profile(enter 0 to exit):");
				input = scan.nextLine();
			}
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

	//Helper method for getting friends of friends for displayFriends
	private void getFriends(String userID, ArrayList<String> list)
	{
		try
		{
			PreparedStatement st1 = dbcon.prepareStatement("SELECT userID1, userID2, name FROM "
															+ "(friends JOIN profile ON (((userID1 = userID) and (userID1 <> ?)) or ((userID2 = userID) and (userID2 <> ?))))"
															+ "WHERE userID1 = ? OR userID2 = ?");
			st1.setString(1, userID);
			st1.setString(2, userID);
			st1.setString(3, userID);
			st1.setString(4, userID);
			ResultSet friends = st1.executeQuery();

			while(friends.next())
			{
				if(userID.equalsIgnoreCase(friends.getString("userID1")))
				{
					System.out.println("\tName: " + friends.getString("name") + ", " + friends.getString("userID2"));
					list.add(friends.getString("userID2"));
				}
				else
				{
					System.out.println("\tName: " + friends.getString("name") + ", " + friends.getString("userID1"));
					list.add(friends.getString("userID1"));
				}
			}

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
	public void createGroup(String name, String desc, int limit)
	{
		try
		{
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO groups values(?, ?, ?, ?)");
			PreparedStatement st2 = dbcon.prepareStatement("INSERT INTO groupMembership values(?, ?, 'manager')");

			//Not sure how to determine gID
			PreparedStatement st3 = dbcon.prepareStatement("SELECT MAX(gID) AS max FROM groups");
			ResultSet gID = st3.executeQuery();
			if(gID.next())
				st1.setInt(1, gID.getInt("max") + 1);
			else
				st1.setInt(1, 1);
			st1.setString(2, name);
			st1.setString(3, desc);
			st1.setInt(4, limit);
			st1.executeUpdate();

			//Add the user to the group as a manager
			st2.setInt(1, gID.getInt("max") + 1);
			st2.setString(2, thisUserID);
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
	public void initiateAddingGroup(String gID)
	{
		try
		{
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter a message for your group request:");
			String msg = scan.nextLine();

			//Add the request to the database
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO pendingGroupmembers values(?, ?, ?)");
			st1.setString(1, gID);
			st1.setString(2, thisUserID);
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
	public void sendMessageToUser(String toID)
	{
		//Prompt the user for a message to send
		//Change so it can be multilined
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your message: ");
		String msg = scan.nextLine();

		try
		{
			PreparedStatement st1 = dbcon.prepareStatement("INSERT INTO messages values(?, ?, ?, ?, NULL, CURRENT_DATE)");
			//Not sure how to determine message ID
			PreparedStatement st2 = dbcon.prepareStatement("SELECT MAX(msgID) as max FROM messages");
			ResultSet id = st2.executeQuery();
			if(id.next())
				st1.setInt(1, id.getInt("max") + 1);
			else
				st1.setInt(1, 1);
			st1.setString(2, thisUserID);
			st1.setString(3, msg);
			st1.setString(4, toID);
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
	public void sendMessageToGroup(String toGroupID)
	{
		try
		{
			//Ask for the message
			Scanner scan = new Scanner(System.in);
			System.out.println("Please enter your message to the group: ");
			String msg = scan.nextLine();

			//First check if the user is in the group
			PreparedStatement st1 = dbcon.prepareStatement("SELECT gID FROM groupMembership WHERE userID = ?");
			st1.setString(1, thisUserID);
			ResultSet groups = st1.executeQuery();

			boolean found = false;
			while(groups.next())
			{
				if(toGroupID.equals(groups.getString("gID")))
				{
					found = true;
					break;
				}
			}

			if(found)
			{
				//Send the message
				PreparedStatement st2 = dbcon.prepareStatement("INSERT INTO messages values(?, ?, ?, NULL, ?, CURRENT_DATE)");
				PreparedStatement st3 = dbcon.prepareStatement("SELECT MAX(msgID) as max FROM messages");
				ResultSet id = st3.executeQuery();
				
				if(id.next()) 
					st2.setInt(1, id.getInt("max") + 1);
				else
					st2.setInt(1, 1);
				st2.setString(2, thisUserID);
				st2.setString(3, msg);
				st2.setString(4, toGroupID);
				st2.executeUpdate();
				System.out.println("Message sent successfully");
			}
			else
			{
				System.out.println("Unable to send message: not a current member of the group");
			}
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
			System.out.println("Message failed to send");
		}
	}

	/*When the user selects this option, the entire contents of every message sent to the user should
	be displayed in a nicely formatted way.*/
	public void displayMessages()
	{
		try
		{
			//Get all the messages sent to the user and order by dateSent descending
			System.out.println("User messages:");
			PreparedStatement st1 = dbcon.prepareStatement("SELECT fromID, message, dateSent FROM (messageRecipient JOIN messages ON (messageRecipient.userID = messages.toUserID)) WHERE toUserID = ? ORDER BY dateSent DESC");
			st1.setString(1, thisUserID);
			ResultSet results = st1.executeQuery();

			//Loop through and display the results
			while(results.next())
			{
				System.out.println("From " + results.getString("fromID"));
				System.out.println("Sent " + results.getDate("dateSent"));
				System.out.println(results.getString("message"));
			}

			System.out.println("Group messages:");
			//Get what groups the user is in
			PreparedStatement st2 = dbcon.prepareStatement("SELECT gID FROM groupMembership WHERE userID = ?");
			st2.setString(1, thisUserID);
			ResultSet groups = st2.executeQuery();

			PreparedStatement st3 = dbcon.prepareStatement("SELECT fromID, message, dateSent FROM (groups JOIN messages ON(groups.gID = messages.toGroupID)) WHERE toGroupID = ? ORDER BY dateSent DESC");

			while(groups.next())
			{
				System.out.println("------------------------------------------------------------------------------------------------------------");
				System.out.println("Group " + groups.getInt("gID"));
				st3.setInt(1, groups.getInt("gID"));
				ResultSet msg = st3.executeQuery();

				while(msg.next())
				{
					System.out.println("From " + msg.getString("fromID"));
					System.out.println("Sent on " + msg.getDate("dateSent"));
					System.out.println(msg.getString("message"));
				}
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
	public void displayNewMessages()
	{
		try
		{
			//Get all messages sent to the user since last login time
			System.out.println("New user messages:");
			PreparedStatement st1 = dbcon.prepareStatement("SELECT fromID, message, dateSent FROM (messageRecipient JOIN messages ON (messageRecipient.userID = messages.toUserID)) WHERE toUserID = ? and dateSent > ? ORDER BY dateSent DESC");
			st1.setString(1, thisUserID);
			st1.setTimestamp(2, last_login);
			ResultSet results = st1.executeQuery();

			//Loop through and display results
			while(results.next())
			{
				System.out.println("From " + results.getString("fromID"));
				System.out.println("Sent " + results.getDate("dateSent"));
				System.out.println(results.getString("message"));
			}

			System.out.println("New group messages:");
			//Get what groups the user is in
			PreparedStatement st2 = dbcon.prepareStatement("SELECT gID FROM groupMembership WHERE userID = ?");
			st2.setString(1, thisUserID);
			ResultSet groups = st2.executeQuery();

			PreparedStatement st3 = dbcon.prepareStatement("SELECT fromID, message, dateSent FROM (groups JOIN messages ON(groups.gID = messages.toGroupID)) WHERE toGroupID = ? and dateSent > ? ORDER BY dateSent DESC");

			while(groups.next())
			{
				System.out.println("------------------------------------------------------------------------------------------------------------");
				System.out.println("Group " + groups.getInt("gID"));
				st3.setInt(1, groups.getInt("gID"));
				st3.setTimestamp(2, last_login);
				ResultSet msg = st3.executeQuery();

				while(msg.next())
				{
					System.out.println("From " + msg.getString("fromID"));
					System.out.println("Sent on " + msg.getDate("dateSent"));
					System.out.println(msg.getString("message"));
				}
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
	public void searchForUser(String request)
	{
		try
		{
			//Tokenize the input
			ArrayList<String> list = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(request);
			while(st.hasMoreTokens())
			{
				list.add(st.nextToken());
			}

			//Prepare the query
			PreparedStatement st1 = dbcon.prepareStatement("SELECT userID, name FROM profile WHERE REGEXP_LIKE(name, ?)");
			PreparedStatement st2 = dbcon.prepareStatement("SELECT userID, name FROM profile WHERE REGEXP_LIKE(userID, ?)");
			PreparedStatement st3 = dbcon.prepareStatement("SELECT userID, name FROM profile WHERE REGEXP_LIKE(email, ?)");

			System.out.println("Here are your results:");

			//Loop over tokens for name
			System.out.println("Matched on name:");
			for(int i = 0; i < list.size(); i++)
			{
				st1.setString(1, list.get(i));
				ResultSet results = st1.executeQuery();

				//Print results of each set
				while(results.next())
				{
					System.out.println("Name: " + results.getString("name"));
					System.out.println("userID: " + results.getString("userID") + "\n");
				}
			}
			System.out.println("============================================");
			
			//Matched on userID
			System.out.println("Matched on userID:");
			for(int i = 0; i < list.size(); i++)
			{
				st2.setString(1, list.get(i));
				ResultSet results = st2.executeQuery();

				//Print results of each set
				while(results.next())
				{
					System.out.println("Name: " + results.getString("name"));
					System.out.println("userID: " + results.getString("userID") + "\n");
				}
			}
			System.out.println("============================================");

			//Matched on email
			System.out.println("Matched on email:");
			for(int i = 0; i < list.size(); i++)
			{
				st3.setString(1, list.get(i));
				ResultSet results = st3.executeQuery();

				//Print results of each set
				while(results.next())
				{
					System.out.println("Name: " + results.getString("name"));
					System.out.println("userID: " + results.getString("userID") + "\n");
				}
			}
			System.out.println("============================================");


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
	public void threeDegrees(String userID1, String userID2)
	{
		try
		{
			//PreparedStatement st1 = dbcon.prepareStatement("SELECT * FROM ");
			
			//Call the recursive helper method
			ArrayList<String> results = new ArrayList<String>();
			ArrayList<String> path = tresDegrees(userID1, userID2, results);

			//Print the results
			if(path == null)
				System.out.println("No path was found between " + userID1 + " and " + userID2);
			else
			{
				for(int i = 0; i < path.size(); i++)
				{
					if(i != path.size() - 1)
						System.out.print(path.get(i) + "->");
					else
						System.out.println(path.get(i));
				}
			}
		}
		//Don't need this for now, no SQL stuff up top
		// catch(SQLException e1)
		// {
		// 	//Print errors
		// 	System.out.println("SQL Error");
		// 	while(e1 != null)
		// 	{
		// 		System.out.println("Message = "+ e1.getMessage());
		// 		System.out.println("SQLState = "+ e1.getSQLState());
		// 		System.out.println("SQLState = "+ e1.getErrorCode());
		// 		e1 = e1.getNextException();
		// 	}
		// }
		catch(Exception e){
			System.out.println(e);
		}
	}

 	/*Display top K who have sent to received the highest number of messages during for the past x
 	months. x and K are input parameters to this function.*/
 	public void topMessages(int k, int x)
 	{
 		try
 		{
 			//Set up the query
 			PreparedStatement st1 = dbcon.prepareStatement("SELECT * FROM "
 														+ "(SELECT fromID, COUNT(msgId) as \"mCount\" FROM messages WHERE dateSent >= ? GROUP BY fromID ORDER BY \"mCount\" DESC) "
 														+ "WHERE rownum <= ? ORDER BY rownum");
 			st1.setInt(2, k);
			
 			//Calculate the date from which to get the messages from
 			Calendar current = Calendar.getInstance();
 			//java.sql.Date date2 = new java.sql.Date(current.getTimeInMillis());
 			//System.out.println(current.get(Calendar.YEAR));
 			current.add(Calendar.MONTH, -x);
 			//System.out.println(current.get(Calendar.MONTH));
 			java.sql.Date date = new java.sql.Date(current.getTimeInMillis());
 			System.out.println(date);
 			st1.setDate(1, date);
 			ResultSet result = st1.executeQuery();

 			//Display the results
 			System.out.println("Top " + k + " message sending users in the past " + x + " months:");
 			int i = 1;
 			while(result.next())
 			{
 				System.out.println(i + ".\t" + result.getString("fromID") + ": " + result.getInt("mCount"));
 				i++;
 			}
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
	public boolean dropUser()
	{
		int rowsDropped = 0;
		try
		{
			//Have triggers handle the details of this
			PreparedStatement st1 = dbcon.prepareStatement("DELETE FROM profile WHERE userID = ?");
			st1.setString(1, thisUserID);
			rowsDropped = st1.executeUpdate();
			// dbcon.close();
			thisUserID = null;
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
		return true;
	}

	/*This option should cleanly shut down and exit the program after marking the time of the user's
logout in the profile relation,*/
	public void Logout()
	{
		//Fix so it can add proper date

		try
		{
			PreparedStatement st2 = dbcon.prepareStatement("UPDATE profile SET lastlogin = CURRENT_TIMESTAMP WHERE userID = ?");
			st2.setString(1, thisUserID);
			st2.executeUpdate();
			// closeDB();
			thisUserID = null;
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

	public void closeDB()
	{
		//Close the connection
		try {
			dbcon.close();
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

	//Recursive helper member find paths for the threeDegrees method
	//Returns null if no path is found, otherwise returns the path in the form of an ArrayList of strings
	private ArrayList<String> tresDegrees(String userID1, String userID2, ArrayList<String> results)
	{
		try
		{
			//Get the friends of userID1
			//System.out.println(results.size());
			if(results.size() < 3)
			{
				//System.out.println("Adding " + userID1);
				results.add(userID1);
				//System.out.println(results);

				PreparedStatement st1 = dbcon.prepareStatement("SELECT userID1, userID2 FROM friends WHERE userID1 = ? OR userID2 = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				st1.setString(1, userID1);
				st1.setString(2, userID1);
				ResultSet friends = st1.executeQuery();

				//Scan the results to check if userID2 if found
				while(friends.next())
				{
					if(userID2.equals(friends.getString("userID1")) || userID2.equals(friends.getString("userID2"))){
						results.add(userID2);
						return results;
					}
				}

				//UserID2 was not found, so recursively check the next level of friends
				friends.beforeFirst();
				ArrayList<String> path = null;
				while(friends.next())
				{
					if(userID1.equals(friends.getString("userID1")) && !results.contains("userID2"))
						path = tresDegrees(friends.getString("userID2"), userID2, results);
					else if(!results.contains("userID1"))
						path = tresDegrees(friends.getString("userID1"), userID2, results);
					if(path != null)
						break;
				}

				//Return the path if found
				if(path != null)
					return path;
				//Else, no path exists from this friend and delete him from the path
				else
				{
					results.remove(results.size() - 1);
					//System.out.println("Removing " + userID1);
					//System.out.println(results);
					return null;
				}
			}
			else
				return null;
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
		return null;
	}
}