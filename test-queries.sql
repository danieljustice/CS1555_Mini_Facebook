--Queries for test data

--Check for profile delete trigger
SELECT * FROM friends where userID1 = 1 or userID2 = 1;

DELETE FROM profile where userID = 1;
commit;
--Should all return nothing
SELECT * FROM friends where userID1 = 1 or userID2 = 1;
SELECT * FROM groupMembership where userID = 1;


--Test select statements from various tables
SELECT * FROM messages where toUserID = 55;
SELECT gID from groups;
SELECT * FROM pendingFriends;
SELECT msgID FROM messageRecipient where userID = 43;


--Check friends_cant_be_pending trigger
INSERT INTO pendingFriends
values(22, 45, 'Hello');
commit;
INSERT INTO friends
values(22, 45, null, 'Yay');
commit;
SELECT * FROM pendingFriends where toID = 45 and fromID = 22;


--Check members_cant_pending trigger
INSERT INTO pendingGroupmembers
values(1, 45, 'Hello');
commit;
INSERT INTO groupMembership
values(1, 45, 'user');
commit;
SELECT * FROM pendingGroupmembers where userID = 45;

--Check group delete trigger
DELETE FROM groups where gID = 1;
commit;
SELECT * FROM messages where gID = 1;

--Check pendingFriends duplicates
INSERT INTO pendingFriends
values(3, 4, 'Hello');
commit;
INSERT INTO pendingFriends
values(4, 3, 'Uh oh');

--Check inserting a group member into pendingGroupmembers
INSERT INTO groupMembership
values(2, 99, 'user');
commit;
INSERT INTO pendingGroupmembers
values(2, 99, 'Throw another error');
commit;

--Check inserting friend pair into pendingFriends
INSERT INTO friends
values(1 , 2, null, 'Yup');
commit;
INSERT INTO pendingFriends
values(1, 2, 'Throw an error please');
commit;

--Check for friend insertion duplicate
INSERT INTO friends
values(4, 5, null, 'Databases can be fun');
commit;
INSERT INTO friends
values(5, 4, null, 'As long as they work properly');
commit;