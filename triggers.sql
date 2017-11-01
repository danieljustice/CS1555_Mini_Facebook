--Here tha be Triggaa's!


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

CREATE OR REPLACE TRIGGER MEMBERS_CANT_BE_PENDING
AFTER
INSERT ON groupMembership
FOR EACH ROW
BEGIN
	DELETE FROM pendingGroupmembers
	WHERE :new.userID = userID;
END;
/

CREATE OR REPLACE TRIGGER CASCADE_MESSAGE_DELETION
AFTER
DELETE ON messages
FOR EACH ROW
BEGIN
	DELETE FROM messageRecipient
	WHERE msgID = :old.msgID;
END;
/

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