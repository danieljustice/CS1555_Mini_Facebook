# CS1555_Mini_Facebook
A school project pioneered by Dan Justice and Jordan Carr

Phase 1 Done

Phase 2 Done

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

bench1.txt
Test logging in, user creation, and logging out.

bench2.txt
initiateFriendship, confirmFriendship, displayFriends

bench3.txt
createGroup, initiateAddingGroup, sendMessageToGroup

bench4.txt
sendMessageToUser, displayMessages, topMessages, displayNewMessages

bench5.txt
threeDegrees, searchForUser, dropUser

BENCHMARK TESTING INSTRUCTIONS
1.	Go to sqlplus and run the ttest.sql file
2.	Go into the benchmarks folder and insert your own sql username and password into the files to run the app on your own profile	
3. 	Run test.sh: this should compile all java files and run the benchmarks