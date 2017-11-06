--Queries for test data

--Check for profile delete trigger
SELECT * FROM friends where userID1 = 1 or userID2 = 1;

DELETE FROM profile where userID = 1;
commit;

SELECT * FROM friends where userID1 = 1 or userID2 = 1;


--Check friends_cant_be_pending trigger
--Waiting on data to be created

--Check members_cant_pending trigger


--Check group delete trigger


--Check insert message recipient trigger