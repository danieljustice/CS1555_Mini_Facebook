import os

server = input("What server are you using? (e.g. 'daj61@unixs.cis.pitt.edu')") or "daj61@unixs.cis.pitt.edu"
filepath = input("What is the file path for these files?") or "/afs/pitt.edu/home/d/a/daj61/private/Team"

# scps = []
files = ["Database.java", "TestDriver.java", "SocialPanther.java", "initDB.sql", "profile.sql", "groups.sql", "friends.sql", "schemas.sql", "triggers.sql"]

fileString = " ".join(files)
scps = ("scp " + fileString + " " + server + ":" + filepath)

os.system(scps)