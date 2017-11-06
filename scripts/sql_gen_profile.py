#!/usr/bin/env python
import random as rand

first_names = [] # Create some last names
last_names = [] # Create some last names

passwords = [] # Create some random passwords

ampm = ['AM', 'PM']


mons = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC']


f = open('profile-insert.sql', 'a')

for i in range (1, 101):
   c_first_name = rand.choice(first_names)
   c_last_name = rand.choice(last_names)
   password = rand.choice(passwords)
   dob_m_u = rand.randint(1, 12)

   dob_m = dob_m_u
   if (dob_m_u < 10):
      dob_m = '0' + str(dob_m_u)

   dob_d_u = rand.randint(1, 28) # Not dealing with date errors
   dob_d = dob_d_u
   if (dob_d_u < 10):
      dob_d = '0' + str(dob_d_u)

   dob_y = 1950 + rand.randint(0, 49)
   dob = '\'' + str(dob_d) + '-' + rand.choice(mons) + '-' + str(dob_y) +  '\''

   ll_day_u = rand.randint(1,28)
   ll_mon_u = rand.randint(1,12)
   ll_day = ll_day_u
   ll_mon = ll_mon_u

   if (ll_day_u < 10):
      ll_day = '0' + str(ll_day_u)

   if (ll_mon_u < 10):
      ll_mon = '0' + str(ll_mon_u)

   ll_year = 2015 + rand.randint(0, 2)

   ll_hour_u = rand.randint(1,12)
   ll_hour = ll_hour_u

   if (ll_hour < 10):
      ll_hour = '0' + str(ll_hour_u)

   ll_min = rand.randint(10, 59)
   ll_sec = rand.randint(10, 59)

   isampm = rand.choice(ampm)

   last_login = '\'' + str(ll_day) + '-' + rand.choice(mons) + '-' + str(ll_year) + ' ' + str(ll_hour) + '.' + str(ll_min) + '.' + str(ll_sec) + '.000000 ' + isampm + '\''
   
   f.write('INSERT INTO profile (userID, name, password, date_of_birth, last_login) VALUES (' + str(i) + ', \'' + c_first_name + ' ' + c_last_name + '\', \'' + password + '\', ' + dob + ', ' + last_login + ');\n' )


f.close()

