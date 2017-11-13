#!/usr/bin/env python
import random as rand
import RandomDate as RandomDate

mons = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC']




messages = ['hi', 'how are you', 'I like pokemon', 'You are awesome'] # Put some messages in here

#number of users in DB
num_of_users = 100
num_of_groups = 10
msg_id = 0
max_messages = 300
f = open('messages-insert.sql', 'w+')
# f.write("commit;\n")
rand.seed(1)
for i in range (1, num_of_users+1):
    #person sending the messages ID;
    from_id = i
    

    #each user can send multiple messages
    #decide who to send a message to, cant message yourself
    number_of_messages = rand.randrange(0, num_of_users)
    for x in range(1, number_of_messages):
        to_id = rand.randrange(-10, num_of_users)
        #stop creating messages for this user if they try to send one to themselves
        if to_id == from_id:
            break
        #if to_id is null, then group_id is a number, and vice versa
        if to_id < 1:
            to_id = 'null'
            group_id = rand.randrange(1, num_of_groups)
        else:
            group_id = 'null'
        #msg_id will start at 1 because it is incremented before being used
        msg_id = msg_id + 1 
        if msg_id > max_messages:
            break
        #get random message
        message = rand.choice(messages)
        

        date_sent = RandomDate.randomDate("1/1/1852 1:30 PM", "11/1/2017 4:50 AM", rand.random())

        f.write("INSERT INTO messages (msgID, fromID, message, toUserID, toGroupID, dateSent) VALUES (" + str(msg_id) + ", " + str(from_id) + ", '" + message + "', " + str(to_id) + ", " + str(group_id) +", " + "TO_TIMESTAMP('" + date_sent + "', 'MM-DD-YYYY HH12:MI:SS'));\n" )    

f.write("commit;\n")
# f.write("commit;\n")
f.close()

