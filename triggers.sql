--Here be tha Triggaa's!


--Delete an entry from pending friends when the corresponding entry is put into friends
CREATE OR REPLACE TRIGGER FRIENDS_CANT_BE_PENDING
AFTER 
INSERT ON friends
FOR EACH ROW
BEGIN 
    DELETE FROM pendingFriends
    WHERE (:new.userID1 = fromID AND
            :new.userID2 = toID) OR
            (:new.userID1 = toID AND
            :new.userID2 = fromID);
END;
/

--Delete everything associated with a profile when that profile is deleted(forced cascade)
CREATE OR REPLACE TRIGGER CASCADE_PROFILE_DELETION
AFTER 
DELETE ON profile
FOR EACH ROW
BEGIN
	DELETE FROM friends
		WHERE userID1 = :old.userID OR userID2 = :old.userID;
	DELETE FROM pendingFriends
		WHERE fromID = :old.userID OR toID = :old.userID;
	DELETE FROM groupMembership
		WHERE userID = :old.userID;
	DELETE FROM pendingGroupmembers
		WHERE userID = :old.userID;
	DELETE FROM messageRecipient
		WHERE userID = :old.userID;
	DELETE FROM messages
		WHERE fromID = :old.userID or toUserID = :old.userID;
END;
/

--Delete an entry from pendingGroupmembers when the corresponding entry is put into groupMembership
CREATE OR REPLACE TRIGGER MEMBERS_CANT_BE_PENDING
AFTER
INSERT ON groupMembership
FOR EACH ROW
BEGIN
	DELETE FROM pendingGroupmembers
	WHERE :new.userID = userID;
END;
/

--Delete everything associated with a messages when that message is deleted(forced cascade)
CREATE OR REPLACE TRIGGER CASCADE_MESSAGE_DELETION
AFTER
DELETE ON messages
FOR EACH ROW
BEGIN
	DELETE FROM messageRecipient
	WHERE msgID = :old.msgID;
END;
/

--Delete everything associated with a group when that group is deleted(forced cascade)
CREATE OR REPLACE TRIGGER CASCADE_GROUP_DELETION
AFTER
DELETE ON groups
FOR EACH ROW
BEGIN
	DELETE FROM groupMembership
	WHERE gID = :old.gID;
	DELETE FROM pendingGroupmembers
	WHERE gID = :old.gID;
	DELETE FROM messages
	WHERE toGroupID = :old.gID;
END;
/

--Add an entry to message recipient when a new message is created
--*Adds entries only for those that go to users, not groups
CREATE OR REPLACE TRIGGER INSERT_MESSAGE_RECIPIENT
AFTER
INSERT ON messages
FOR EACH ROW
WHERE toUserID IS NOT NULL
BEGIN
	INSERT INTO messageRecipient
		values(:new.msgID, :new.toUserID);
END;
/