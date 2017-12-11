# CS1555_Mini_Facebook
A school project pioneered by Dan Justice and Jordan Carr

Phase 1 Done

Phase 2 Done

Phase 3 Done

How to run our program:
SocialPanther.java - main driver
Database.java - database related functions

Setting up our database:
schemas.sql - tables, schemas, constraints
triggers.sql - all related triggers

Setting up the sample data (needed for our benchmarks):
profile.sql
groups.sql
friendship-insert.sql
messages-insert.sql


How to test the benchmarks:

The benchmarks are a series of text files desinged as sample user input used for testing the sturdiness of our database.
The first two lines in each file is your username and password to log in to Oracle.

WHEN TESTING THESE BENCHMARKS, PLEASE BE SURE TO INSERT YOUR OWN ORACLE USERID AND PASSWORD THERE!!!!!!

Along with valid input, the benchmarks also include invalid input to test that the user isn't passing in 
invalid data.

Please run the benchmarks in the order presented below: the first file creates the inital user that logs in for each 
subsequent test. All tests can be run at once with the test.sh shell script. HOWEVER, please be sure to run ttest.sql
in sqlplus first in order to set up the databases.

Also, be sure to insert all of our datafiles to ensure everything works properly.

A brief description of the files:

*Note: Some of the benchmarks will produce sql errors and other signs of invalid data. This is on purpose and should happen.*

bench1.txt
Test logging in, user creation, and logging out. Signs up a new user, entering garbage data for error checks and logs them in.
Tries to sign up another user with the same username and should fail on purpose.

bench2.txt
initiateFriendship, confirmFriendship, displayFriends. Send some error requests, sending for duplicates to make sure they aren't sent.
Logs in as another user and accepts that request. Displays waiting requests to make sure it no longer exists in the table. Displays the 
friends of the user and goes through some of the mentioned profiles.

bench3.txt
createGroup, initiateAddingGroup, sendMessageToGroup
Creates a group, logs in as another user and requests permission to join the group. Logs back in as the original user to confirm
the request. Sends a messages to the group to make sure everyone in the group recieves it.

bench4.txt
sendMessageToUser, displayMessages, topMessages, displayNewMessages
Sends a messages to a non-friend to check for errors. Sends a message to a friend. Checks the top k messages for the past 100
months. Displays new messages for the person who recieved the inital message from above. Displays all regular messages for the user.

*Note, please be aware that due to the way our program handles multiline messages bench3 and bench4 may not run correctly.
	These files may need simulated manually if that is the case.*

bench5.txt
threeDegrees, searchForUser, dropUser
Searches various paths for 3 degrees. Searches for various items to match for. Drops the user created in bench1 to ensure everything.

BENCHMARK TESTING INSTRUCTIONS
1.	Go to sqlplus and run the ttest.sql file
2.	Go into the benchmarks folder and your own sql username and password into the files to run the app on your own profile.	
	These are the first two lines of the files and currenly contain fillers for you to use.
3. 	Run test.sh: this should compile all java files and run the benchmarks
4.	After every run through of the benchmarks please remember to reset ttest.sql.

