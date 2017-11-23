import java.sql.*;

public class Database
{
	private static Connection dbcon;

	public Database()
	{

	}

	//Implement the following functions

	//Given a name, email address, and date of birth, add a new user to the system by inserting as
	//new entry in the profile relation.
	public void createUser(String userID, String name, String password, String dob, String last_login, String email)
	{	
		//Add something to convert the date to a timestamp
		try
		{
			PreparedStatement st1 = dbcon.PreparedStatement("INSERT INTO profile values(?, ?, ?, ?, ?, ?)");
			st1.setString(1, userID);
			st1.setString(2, name);
			st1.setString(3, password);
			st1.setString(4, dob);
			st1.setString(5, last_login);
			st1.setString(6, email);
			st1.executeQuery();
		}
		catch(SQLException e1)
		{
			System.out.println("SQL Error");
			while(e1 != null)
			{
				System.out.println("Message = "+ e1.getMessage());
				System.out.println("SQLState = "+ e1.getSQLstate());
				System.out.println("SQLState = "+ e1.getErrorCode());
				e1 = e1.getNextException();
			}
		}
	}

	//Given userID and password, login as the user in the system when an appropriate match is
	//found
	public boolean Login()
	{

	}

	//Create a pending friendship from the (logged in) user to another user based on userID. The
	//application should display the name of the person that will be sent a friends request and the user
	//should be prompted to enter a message to be sent along with the request. A last confirmation
	//should be requested of the user before an entry is inserted into the pendingFriends relation,
	//and success or failure feedback is displayed for the user.
	public boolean initiateFriendship()
	{

	}

	//This task should first display a formatted, numbered list of all outstanding friends and group
	//requests with an associated messages. Then, the user should be prompted for a number of the
	//request he or she would like to confirm or given the option to confirm them all. The application
	//should move the request from the appropriate pendingFriends or pendingGroupmembers
	//relation to the friends or groupMembership relation. The remaining requests which were not
	//selected are declined and removed from pendingFriends and pendingGroupmembers relations.
	public void confirmFriendship()
	{

	}

	/*This task supports the browsing of the user’s friends and of their friends’ profiles. It first
	displays each of the user’s friends’ names and userIDs and those of any friend of those friends.
	Then it allows the user to either retrieve a friend’s entire profile by entering the appropriate
	userID or exit browsing and return to the main menu by entering 0 as a userID. When selected,
	a friend’s profile should be displayed in a nicely formatted way, after which the user should be
	prompted to either select to retrieve another friend’s profile or return to the main menu.*/
	public void displayFriends()
	{

	}

	/*Given a name, description, and membership limit, add a new group to the system, add the
	user as its first member with the role manager.*/
	public void createGroup()
	{

	}

	/*Given a user and a group, create a pending request of adding to group (if not violate the
	group’s membership limit). The user should be prompted to enter a message to be sent along
	with the request and inserted in the pendingGroupmembers relation.*/
	public void initiateAddingGroup()
	{

	}

	/*With this the user can send a message to one friend given his userID. The application should
	display the name of the recipient and the user should be prompted to enter the text of the
	message, which could be multi-lined. Once entered, the message should be “sent” to the user
	by adding appropriate entries into the messages and messageRecipients relations by creating
	a trigger. The user should lastly be shown success or failure feedback.*/
	public void sendMessageToUser()
	{

	}

	/*With this the user can send a message to a recipient group, if the user is within the group.
	Every member of this group should receive the message. The user should be prompted to enter
	the text of the message, which could be multi-lined. Then the created new message should
	be “sent” to the user by adding appropriate entries into the messages and messageRecipients
	relations by creating a trigger. The user should lastly be shown success or failure feedback.
	Note that if the user sends a message to one friend, you only need to put the friend’s userID
	to ToUserID in the table of messages. If the user wants to send a message to a group, you need
	to put the group ID to ToGroupID in the table of messages and use a trigger to populate the
	messageRecipient table with proper user ID information as defined by the groupMembership
	relation.*/
	public void sendMessageToGroup()
	{

	}

	/*When the user selects this option, the entire contents of every message sent to the user should
	be displayed in a nicely formatted way.*/
	public void displayMessages()
	{

	}

	/*This should display messages in the same fashion as the previous task except that only those
	messages sent since the last time the user logged into the system should be displayed.*/
	public void displayNewMessages()
	{

	}

	/*Given a string on which to match any user in the system, any item in this string must be
	matched against any significant field of a user’s profile. That is if the user searches for “xyz
	abc”, the results should be the set of all profiles that match “xyz” union the set of all profiles
	that matches “abc”*/
	public void searchForUser()
	{

	}

	/*Given two users (A and B), find a path, if one exists, between A and B with at most 3 hop
	between them. A hop is defined as a friendship between any two users.*/
	public void threeDegrees()
	{

	}

	/*Display top K who have sent to received the highest number of messages during for the past x
	months. x and K are input parameters to this function.*/
	public void topMessages()
	{

	}

	/*Remove a user and all of their information from the system. When a user is removed, the system
	should delete the user from the groups he or she was a member of using a trigger. Note:
	messages require special handling because they are owned by both sender and receiver. Therefore,
	a message is deleted only when both he sender and all receivers are deleted. Attention
	should be paid handling integrity constraints.*/
	public void dropUser()
	{

	}

	/*This option should cleanly shut down and exit the program after marking the time of the user’s
logout in the profile relation,*/
	public void Logout()
	{

	}

	//Perform tests for the functions
	public static void main(String args[])
	{
		//Ask for username and password to Oracle

		//Open the connection
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
		dbcon = DriverManager.getConnection(url, username, password);

		//tests for the above methods

		//Close the connection
		dbcon.close();
	}
}


10. displayMessages

11. displayNewMessages

12. searchForUser

13. threeDegress

14. topMessages

4
15. dropUser

16. LogOut

