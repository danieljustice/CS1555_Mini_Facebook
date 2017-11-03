lines = []
while True:
    line = input()
    if line:
        lines.append(line)
    else:
        break

inserts = []

for line in lines:
    newString = line[:-23] + "TO_TIMESTAMP(" + line[-23:-2] + ", 'YYYY-MM-DD HH24:MI:SS'));"
    print(newString)