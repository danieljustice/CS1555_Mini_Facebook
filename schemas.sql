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
	CONSTRAINT profile_pk PRIMARY KEY (userID));

CREATE TABLE friends(
	userID1 varchar2(20),
	userID2 varchar2(20),
	JDate date,
	message varchar2(200),
	CONSTRAINT friends_pk PRIMARY KEY(userID1, userID2, JDate),
	CONSTRAINT friends_fk1 FOREIGN KEY (userID1) REFERENCES profile(userID),
	CONSTRAINT friends_fk FOREIGN KEY (userID2) REFERENCES profile(userID));

CREATE TABLE pendingFriends(
	fromID varchar2(20),
	toID varchar2(20),
	message varchar2(200),
	CONSTRAINT pendingFriends_pk PRIMARY KEY (fromID, toID),
	CONSTRAINT pendingFriends_FK1 FOREIGN KEY (fromID) REFERENCES profile(userID),
	CONSTRAINT pendingFriends_FK2 FOREIGN KEY (toID) REFERENCES profile(userID));

CREATE TABLE messages(
	msgID varchar2(20),
	fromID varchar2(20),
	message varchar2(200),
	toUserID varchar2(20),
	toGroupID varchar2(20),
	dateSend date,
	CONSTRAINT messages_pk PRIMARY KEY (msgID),
	CONSTRAINT messages_fk1 FOREIGN KEY (toUserID) REFERENCES profile(userID),
	CONSTRAINT messages_fk2 FOREIGN KEY (toGroupID) REFERENCES groups(gID));