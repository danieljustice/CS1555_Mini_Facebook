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
--CREATE OR REPLACE TRIGGER CASCADE_PROFILE_DELETION
--AFTER 
--DELETE ON profile
--FOR EACH ROW
--BEGIN
	--DELETE FROM friends
		--WHERE userID1 = :old.userID OR userID2 = :old.userID;
	--DELETE FROM pendingFriends
		--WHERE fromID = :old.userID OR toID = :old.userID;
	--DELETE FROM groupMembership
		--WHERE userID = :old.userID;
	--DELETE FROM pendingGroupmembers
		--WHERE userID = :old.userID;
	--DELETE FROM messageRecipient
		--WHERE userID = :old.userID;
	--DELETE FROM messages
		--WHERE fromID = :old.userID or toUserID = :old.userID;
--END;
--/

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
--CREATE OR REPLACE TRIGGER CASCADE_MESSAGE_DELETION
--AFTER
--DELETE ON messages
--FOR EACH ROW
--BEGIN
	--DELETE FROM messageRecipient
	--WHERE msgID = :old.msgID;
--END;
--/

--Delete everything associated with a group when that group is deleted(forced cascade)
--CREATE OR REPLACE TRIGGER CASCADE_GROUP_DELETION
--AFTER
--DELETE ON groups
--FOR EACH ROW
--BEGIN
	--DELETE FROM groupMembership
	--WHERE gID = :old.gID;
	--DELETE FROM pendingGroupmembers
	--WHERE gID = :old.gID;
	--DELETE FROM messages
	--WHERE toGroupID = :old.gID;
--END;
--/

--Add an entry to message recipient when a new message is created
--*Adds entries only for those that go to users, not groups
CREATE OR REPLACE TRIGGER INSERT_MESSAGE_RECIPIENT
AFTER
INSERT ON messages
FOR EACH ROW
BEGIN
	if :new.toUserID is not null then
		INSERT INTO messageRecipient
			values(:new.msgID, :new.toUserID);
	end if;
END;
/

CREATE OR REPLACE TRIGGER FRIEND_DUPLICATE_CHECK
BEFORE
INSERT ON friends
FOR EACH ROW
DECLARE qty number:= 0;
BEGIN
	select count(*) into qty from friends where userID1 = :new.userID2 and userID2 = :new.userID1;
	if qty > 0 then
		raise_application_error(-20001, 'Duplicate value inserted.');
	end if;
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
		raise_application_error(-20002, 'Duplicate value inserted.');
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
		raise_application_error(-20003, 'No a group member cannot be inserted into the pendingGroupmembers table.');
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
		raise_application_error(-20004, 'A friend request cannot be made by already existing friends.');
	end if;
END;
/

--Add a message to messageRecipients
--CREATE OR REPLACE TRIGGER ADD_TO_RECIPIENTS
--AFTER
--INSERT ON messages
--FOR EACH ROW
--BEGIN
	--id = :new.msgID;
	--IF :new.toGroupID = null then
		--INSERT INTO messageRecipient values(:new.msgID, :new.toUserID);

	--ELSE
		--INSERT INTO messageRecipient (msgID, userID)
		--SELECT msgID, userID FROM (groupMembership JOIN messages ON (gID = toGroupID));
		--WHERE msgID = :new.msgID;
	--END IF;
--END;
--/



--The following are all similar and have a problem on what the message Id should be
--Add a message when a pair is added to pendingfriends
--CREATE OR REPLACE TRIGGER ADD_PF_MESSAGE
--AFTER
--INSERT ON pendingFriends
--FOR EACH ROW
--DECLARE qty number:= 0;
--BEGIN
	--SELECT MAX(msgID) INTO qty FROM messageRecipient;
	--INSERT INTO messages
	--values(qty + 1, :new.message, :new.fromID, :new.toID, null, null);
--END;
--/

--Add a message when a pair is added to friends
--CREATE OR REPLACE TRIGGER ADD_F_MESSAGE
--AFTER
--INSERT ON friends
--FOR EACH ROW
--DECLARE qty number:= 0;
--BEGIN
	--SELECT MAX(msgID) INTO qty FROM messageRecipient;
	--INSERT INTO messages
	--values(qty + 1, :new.message, :new.userID1, :new.userID2, null, null);
--END;
--/

--When a profile is deleted, set references to it in messages to null
--and delete messages that have no references to them
--CREATE OR REPLACE TRIGGER PROFILE_USER_TO_NULL
--BEFORE
--DELETE ON profile
--FOR EACH ROW
--BEGIN
	--UPDATE messages SET toUserID = NULL
		--WHERE toUserID = :old.userID;
	--UPDATE messages SET fromID = NULL
		--WHERE fromID = :old.userID;
	--DELETE FROM messages
		--WHERE toUserID = NULL and fromID = NULL;
--END;
--/

-- Delete a message where the recipient and userID have been deleted
CREATE OR REPLACE TRIGGER DELETE_MESSAGE
AFTER
DELETE ON profile
BEGIN
	DELETE FROM messages
	-- WHERE toUserID is null and fromID is null and toGroupID is null;
	where msgID in (SELECT msgID
			FROM messages
			WHERE fromID = NULL and toUserID = NULL and toGroupID = NULL);
	-- DELETE_MESSAGES_ON_NULL_USERS(300);
END;
/

-- CREATE OR REPLACE PROCEDURE DELETE_MESSAGES_ON_NULL_USERS(msg in number)
-- AS
-- WOOT number;
-- BEGIN
-- 	for i in (select * 
-- 		FROM messages)LOOP
-- 				dbms_output.put_line('I have '||i.cnt||' male friends.');
-- 			-- if i.fromID = null then
-- 			-- DELETE from messages where msgID = i.msgID;
-- 		END LOOP;


-- select msgID into WOOT from messages where msgID = msg;
-- if msg = 5 then
-- 	dbms_output.put_line ('msg is ' || WOOT);
-- ELSE
-- dbms_output.put_line ('balance is too low');
-- end if;
-- 	-- dbm_output.put_line('TEsting this.');
-- END; 
-- -- DELETE_MESSAGES_ON_NULL_USERS;
-- /

--Insert into message recipients group messages
CREATE OR REPLACE TRIGGER ADD_GM_TO_RECIPIENTS
AFTER
INSERT ON messages
FOR EACH ROW WHEN(new.toGroupId  is not null)
BEGIN
	--IF :new.toGroupID <> null then
		INSERT INTO messageRecipient (msgID, userID)
		SELECT :new.msgID, userID FROM groupMembership
		WHERE gID = :new.toGroupID;
	--END IF; 
END;
/