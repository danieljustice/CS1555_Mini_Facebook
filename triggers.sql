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
--Needs fixed
CREATE OR REPLACE TRIGGER INSERT_MESSAGE_RECIPIENT
AFTER
INSERT ON messages
FOR EACH ROW
BEGIN
	INSERT INTO messageRecipient
		values(:new.msgID, :new.toUserID);
END;
/

--Insert inverse of the friend pair to make duplicate checking easier
CREATE OR REPLACE TRIGGER FRIEND_DUPLICATE_INSERT
AFTER
INSERT ON friends
FOR EACH ROW
BEGIN
	INSERT INTO friends
		values(:new.userID2, :new.userID1, :new.JDate, :new.message);
END;
/

--Delete the inverse of the friend pair
CREATE OR REPLACE TRIGGER CASCACE_FRIEND_DELETION
AFTER
DELETE ON friends
FOR EACH ROW
BEGIN
	DELETE FROM friends
	WHERE userID1 = :old.userID2 and userID2 = :old.userID1;
END;
/

--Check for duplicate values in pendingFriends
CREATE OR REPLACE TRIGGER PENDINGFRIENDS_DUPLICATE_CHECK
BEFORE 
INSERT ON pendingFriends
FOR EACH ROW
DECLARE qty number := 0;
BEGIN
	select count(*) into qty from pendingFriends where toID = :new.fromID and fromID = :new.toID;
	if qty > 0 then
		ROLLBACK;
	end if;
END;
/

--Check that a pendingGroupmember isn't already in the group
CREATE OR REPLACE TRIGGER PENDING_GM_NOT_IN_GROUP
BEFORE
INSERT ON pendingGroupmembers
FOR EACH ROW
DECLARE qty number := 0;
BEGIN
	SELECT COUNT(*) INTO qty from groupMembership where userID = :new.userID and gID = :new.userID;
	if qty > 0 then
		ROLLBACK;
	end if;
END;
/

--Check that pendingFriends aren't already friends
CREATE OR REPLACE TRIGGER PENDING_F_NOT_IN_FRIENDS
BEFORE
INSERT ON pendingFriends
FOR EACH ROW
DECLARE qty number := 0;
BEGIN
	SELECT COUNT(*) INTO qty FROM friends where userID1 = :new.toID and userID2 = :new.fromID;
	if qty > 0 then
		ROLLBACK;
	end if;
END;
/