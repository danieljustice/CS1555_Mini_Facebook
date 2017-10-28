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