import os

server = input("What server are you using? (e.g. 'daj61@unixs.cis.pitt.edu')") or "daj61@unixs.cis.pitt.edu"
filepath = input("What is the file path for these files?") or "/afs/pitt.edu/home/d/a/daj61/private/Team"

# JORDAN - fill in the file path yourself
if(server == '1'):
    server = "jcc117@unixs.cis.pitt.edu"
    filepath = ""


# scps = []
files = ["Database.java", "TestDriver.java", "SocialPanther.java", "initDB.sql", "profile.sql", "groups.sql", "friends.sql", "schemas.sql", "triggers.sql", "benchmarks/bench1.txt", "benchmarks/bench2.txt"]

fileString = " ".join(files)
scps = ("scp " + fileString + " " + server + ":" + filepath)

os.system(scps)