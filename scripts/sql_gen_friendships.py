#!/usr/bin/env python
import random as rand
import RandomDate as RandomDate




messages = ["", "Let us be friends!", "yo friend"] # put whatever messages you want in here


f = open('friendship-insert.sql', 'a')

rand.seed(1)
max_friendships = 200
max_users = 100

for user1 in range (1, max_users):
      if(user1 == max_users):
            break
      user2 = rand.randrange(user1+1, max_users+1)
      date_sent = RandomDate.randomDate("1/1/1852 1:30 PM", "11/1/2017 4:50 AM", rand.random())
      while user2 < max_users+1:
            if(user2 == max_users):
                  break
            f.write("INSERT INTO friends (userID1, userID2, JDate, message) VALUES (" + str(user1) + ", " + str(user2) + ", " + "TO_DATE('" +  date_sent + "', 'MM/DD/YYYY HH12:MI')" + ", '" + rand.choice(messages) + "');\n" )

            user2 = rand.randrange(user2+1, max_users+1)

f.close()

