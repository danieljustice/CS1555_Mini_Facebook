#!/usr/bin/env python
import random as rand

gname = [] # put some random names in here	

f = open('group-insert.sql', 'a')

for i in range (1, 11):
   name = rand.choice(gname)
   f.write('INSERT INTO groups (gID, name, description) VALUES (' + str(i) + ', \'' + name + '\', \'' + name +  ' group\');\n' );


f.close()

