#!/bin/sh
# Run benchmark tests for SocialPanther

# Please be sure to set up all the databases first by going into
# sqlplus and launching the ttest.sql file

javac SocialPanther.java
echo "Testing profile creation, login, logoff"
java SocialPanther<benchmarks/bench1.txt
echo "Testing initiateFrienship, confirmFriendship, displayFriends"
java SocialPanther<benchmarks/bench2.txt
echo "Testing createGroup, initateAddingGroup, sendMessageToGroup"
java SocialPanther<benchmarks/bench3.txt
echo "Testing sendMessageToUser, displayMessages, displayNewMessages, topMessages"
java SocialPanther<benchmarks/bench4.txt
echo "Testing threeDegrees, searchForUser, dropUser"
java SocialPanther<benchmarks/bench5.txt
