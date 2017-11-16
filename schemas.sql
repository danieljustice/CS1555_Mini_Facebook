--Jordan Carr and Daniel Justice
--CS1555
--Social@Panther Database, Schemas,


--Drop all tables
DROP TABLE profile CASCADE CONSTRAINTS;
DROP TABLE friends CASCADE CONSTRAINTS;
DROP TABLE pendingFriends CASCADE CONSTRAINTS;
DROP TABLE messages CASCADE CONSTRAINTS;
DROP TABLE messageRecipient CASCADE CONSTRAINTS;
DROP TABLE groups CASCADE CONSTRAINTS;
DROP TABLE groupMembership CASCADE CONSTRAINTS;
DROP TABLE pendingGroupmembers CASCADE CONSTRAINTS;
--DROP TYPE role_domain;

--Create all tables associated with the database
--Assume many people can share an email
CREATE TABLE profile(
	userID varchar2(20) not null,
	name varchar2(50),
	password varchar2(50),
	date_of_birth date,
	lastlogin timestamp,
	email varchar2(50),
	CONSTRAINT profile_pk PRIMARY KEY (userID)
);

--Assume that userID1 and userID2 make a set
--For example, the userID pairs {1, 2} and {2, 1} would be considered duplicates
--Also assume the message is tied to the friendship and isn't considered a message to any individual
CREATE TABLE friends(
	userID1 varchar2(20) not null,
	userID2 varchar2(20) not null,
	JDate date,
	message varchar2(200),
	CONSTRAINT friends_pk PRIMARY KEY(userID1, userID2),
	CONSTRAINT friends_fk1 FOREIGN KEY (userID1) REFERENCES profile(userID) ON DELETE CASCADE,
	CONSTRAINT friends_fk FOREIGN KEY (userID2) REFERENCES profile(userID) ON DELETE CASCADE,
	CONSTRAINT friends_id_check CHECK (userID1 <> userID2)
);

--Assume the same logic of duplicates as the friend table, where fromID and toID make a set
--Assume the message is tied to the request itself and is not a message to any individual
CREATE TABLE pendingFriends(
	fromID varchar2(20) not null,
	toID varchar2(20) not null,
	message varchar2(200),
	CONSTRAINT pendingFriends_pk PRIMARY KEY (fromID, toID),
	CONSTRAINT pendingFriends_FK1 FOREIGN KEY (fromID) REFERENCES profile(userID) ON DELETE CASCADE,
	CONSTRAINT pendingFriends_FK2 FOREIGN KEY (toID) REFERENCES profile(userID) ON DELETE CASCADE,
	CONSTRAINT pendindingFriends_id_check CHECK (fromID <> toID)
);

CREATE TABLE groups(
	gID				varchar2(20) not null,
	name			varchar2(50),
	description		varchar2(200),
	CONSTRAINT groups_ID PRIMARY KEY (gID)
);

--Assume a user cannot send a message to themselves
CREATE TABLE messages(
	msgID varchar2(20) not null,
	fromID varchar2(20),
	message varchar2(200),
	toUserID varchar2(20),
	toGroupID varchar2(20),
	dateSent date,
	CONSTRAINT messages_pk PRIMARY KEY (msgID),
	CONSTRAINT messages_fk1 FOREIGN KEY (toUserID) REFERENCES profile(userID) ON DELETE CASCADE,
	CONSTRAINT messages_fk2 FOREIGN KEY (toGroupID) REFERENCES groups(gID) ON DELETE CASCADE,
	CONSTRAINT messages_selfsend_check CHECK (fromID <> toUserID));

ALTER TABLE messages modify toGroupID default null;
ALTER TABLE messages modify toUserID default null;

CREATE TABLE messageRecipient(
	msgID			varchar2(20) not null,
	userID			varchar2(20) not null,
	CONSTRAINT messageRecipient_pk PRIMARY KEY (msgID, userID),
	CONSTRAINT messageRecipient_fk1 FOREIGN KEY (msgID) REFERENCES messages(msgID) ON DELETE CASCADE,
	CONSTRAINT messageRecipient_fk2 FOREIGN KEY (userID) REFERENCES profile(userID) ON DELETE CASCADE
);

--Assume a group member only has one role
CREATE TABLE groupMembership(
	gID				varchar2(20) not null,
	--user that is a part of the group
	userID			varchar2(20) not null,
	--roles are manager or member
	role			varchar(20) not null,
	--primary key is a combo of the group ID and a user in that group
	CONSTRAINT groupMembership_pk PRIMARY KEY (gID, userID),
	CONSTRAINT groupMembership_fk1 FOREIGN KEY (gID) REFERENCES groups(gID) ON DELETE CASCADE,
	CONSTRAINT groupMembership_fk2 FOREIGN KEY (userID) REFERENCES profile(userID) ON DELETE CASCADE,
	CONSTRAINT groupMembership_check_role CHECK (role IN ('manager', 'user'))
);

--Assume the message is tied to the membership request and is not sent to an individual
CREATE TABLE pendingGroupmembers(
	gID				varchar2(20) not null,
	--user who wants to joing the group
	userID			varchar2(20) not null,
	message 		varchar2(200),
	--primary key is a combo of the group to be joined and the user that wants to join
	CONSTRAINT pendingGroupmembers_pk PRIMARY KEY (gID, userID),
	CONSTRAINT pendingGroupmembers_fk1 FOREIGN KEY (gID) REFERENCES groups(gID) ON DELETE CASCADE,
	CONSTRAINT pendingGroupmembers_fk2 FOREIGN KEY (userID) REFERENCES profile(userID) ON DELETE CASCADE
);

commit;
