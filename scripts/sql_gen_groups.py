#!/usr/bin/env python
import random as rand

gname = ["Pokemon Club", "Golf Club", "Yodeling Group"] # put some random names in here	

f = open('group-insert.sql', 'w+')
rand.seed(1)
for i in range (1, 11):
   name = rand.choice(gname)
   f.write('INSERT INTO groups (gID, name, description) VALUES (' + str(i) + ', \'' + name + '\', \'' + name +  ' group\');\n' )

f.write("commit;\n")
f.close()

