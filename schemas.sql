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
	userID varchar2(20) not null,
	name varchar2(50),
	password varchar2(50),
	date_of_birth date,
	lastlogin timestamp,
	CONSTRAINT profile_pk PRIMARY KEY (userID)
);

CREATE TABLE friends(
	userID1 varchar2(20) not null,
	userID2 varchar2(20) not null,
	JDate date not null,
	message varchar2(200),
	CONSTRAINT friends_pk PRIMARY KEY(userID1, userID2, JDate),
	CONSTRAINT friends_fk1 FOREIGN KEY (userID1) REFERENCES profile(userID),
	CONSTRAINT friends_fk FOREIGN KEY (userID2) REFERENCES profile(userID)
);

CREATE TABLE pendingFriends(
	fromID varchar2(20) not null,
	toID varchar2(20) not null,
	message varchar2(200),
	CONSTRAINT pendingFriends_pk PRIMARY KEY (fromID, toID),
	CONSTRAINT pendingFriends_FK1 FOREIGN KEY (fromID) REFERENCES profile(userID),
	CONSTRAINT pendingFriends_FK2 FOREIGN KEY (toID) REFERENCES profile(userID)
);

CREATE TABLE messages(
	msgID varchar2(20) not null,
	fromID varchar2(20),
	message varchar2(200),
	toUserID varchar2(20),
	toGroupID varchar2(20),
	dateSend date,
	CONSTRAINT messages_pk PRIMARY KEY (msgID),
	CONSTRAINT messages_fk1 FOREIGN KEY (toUserID) REFERENCES profile(userID),
	CONSTRAINT messages_fk2 FOREIGN KEY (toGroupID) REFERENCES groups(gID));

ALTER TABLE messages modify toGroupID default null;
ALTER TABLE messages modify toUserID default null;

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
