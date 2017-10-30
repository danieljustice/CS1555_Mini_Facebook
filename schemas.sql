--Jordan Carr and Daniel Justice
--CS1555
--Social@Panther Database, Schemas, Triggers, and Constraints


--Drop all tables
DROP TABLE profile CASCADE CONSTRAINTS;
DROP TABLE friends CASCADE CONSTRAINTS;
DROP TABLE pendingFriends CASCADE CONSTRAINTS;
DROP TABLE messages CASCADE CONSTRAINTS;
DROP TABLE messageRecipient CASCADE CONSTRAINTS;
DROP TABLE groups CASCADE CONSTRAINTS;
DROP TABLE groupMembership CASCADE CONSTRAINTS;
DROP TABLE pendingGroupmembers CASCADE CONSTRAINTS;

CREATE TABLE profile(
	userID varchar2(20),
	name varchar2(50),
	password varchar2(50),
	date_of_birth date,
	lastlogin timestamp,
	CONSTRAINT profile_pk PRIMARY KEY (userID)
);








CREATE TABLE messageRecipient(
	msgID			varchar2(20),
	userID			varchar2(20),
	CONSTRAINT messageRecipient_pk PRIMARY KEY (msgID, useID),
	CONSTRAINT messageRecipient_fk1 FOREIGN KEY (msgID) REFERENCES messages(msgID),
	CONSTRAINT messageRecipient_fk2 FOREIGN KEY (userID) REFERENCES profile(userID)
);


CREATE TABLE groups(
	gID				varchar2(20),
	name			varchar2(50),
	description		varchar2(200),
	CONSTRAINT groups_ID PRIMARY KEY (gID)
);

CREATE TABLE groupMembership(
	gID				varchar2(20),
	--user that is a part of the group
	userID			varchar2(20),
	--roles are manager or member
	role			varchar2(20),
	--primary key is a combo of the group ID and a user in that group
	CONSTRAINT groupMembership_pk PRIMARY KEY (gID, userID),
	CONSTRAINT groupMembership_fk1 FOREIGN KEY gID REFERENCES groups(gID),
	CONSTRAINT groupMembership_fk2 FOREIGN KEY userID REFERENCES profile(userID)
);

CREATE TABLE pendingGroupmembers(
	gID				varchar2(20),
	--user who wants to joing the group
	userID			varchar2(20),
	message 		varchar2(200),
	--primary key is a combo of the group to be joined and the user that wants to join
	CONSTRAINT pendingGroupmembers_pk PRIMARY KEY (gID, userID),
	CONSTRAINT pendingGroupmembers_fk1 FOREIGN KEY gID REFERENCES groups(gID),
	CONSTRAINT pendingGroupmembers_fk2 FOREIGN KEY userID REFERENCES profile(userID)
);