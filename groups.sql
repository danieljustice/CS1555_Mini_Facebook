--Insert all data referring to groups

INSERT INTO groups values(1, 'Cs', 'CS Department Group');
INSERT INTO groups values(2, 'Math', 'Math Department Group');
INSERT INTO groups values(3, 'Band', 'Pitt Band Group');
INSERT INTO groups values(4, 'JPNSE', 'Japanese Department Group');
INSERT INTO groups values(5, 'GameDev', 'Game Development Group');
INSERT INTO groups values(6, 'Engr', 'Engineering Group');
INSERT INTO groups values(7, 'Music', 'Music Department Group');
INSERT INTO groups values(8, 'Bio', 'Biology Department Group');
INSERT INTO groups values(9, 'Youtube', 'Youtube Group');
INSERT INTO groups values(10, 'Sports', 'Sports Group');

--Insert members into each group

INSERT INTO groupMembership (userID, gID, role) values(1, 1, 'user');
INSERT INTO groupMembership (userID, gID, role) values(2, 1, 'user');
INSERT INTO groupMembership (userID, gID, role) values(3, 1, 'user');
INSERT INTO groupMembership (userID, gID, role) values(4, 1, 'user');
INSERT INTO groupMembership (userID, gID, role) values(5, 1, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(11, 2, 'user');
INSERT INTO groupMembership (userID, gID, role) values(25, 2, 'user');
INSERT INTO groupMembership (userID, gID, role) values(3, 2, 'user');
INSERT INTO groupMembership (userID, gID, role) values(6, 2, 'user');
INSERT INTO groupMembership (userID, gID, role) values(54, 2, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(100, 3, 'user');
INSERT INTO groupMembership (userID, gID, role) values(89, 3, 'user');
INSERT INTO groupMembership (userID, gID, role) values(32, 3, 'user');
INSERT INTO groupMembership (userID, gID, role) values(44, 3, 'user');
INSERT INTO groupMembership (userID, gID, role) values(63, 3, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(7, 4, 'user');
INSERT INTO groupMembership (userID, gID, role) values(34, 4, 'user');
INSERT INTO groupMembership (userID, gID, role) values(13, 4, 'user');
INSERT INTO groupMembership (userID, gID, role) values(56, 4, 'user');
INSERT INTO groupMembership (userID, gID, role) values(70, 4, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(99, 5, 'user');
INSERT INTO groupMembership (userID, gID, role) values(37, 5, 'user');
INSERT INTO groupMembership (userID, gID, role) values(25, 5, 'user');
INSERT INTO groupMembership (userID, gID, role) values(52, 5, 'user');
INSERT INTO groupMembership (userID, gID, role) values(50, 5, 'manager')

INSERT INTO groupMembership (userID, gID, role) values(9, 6, 'user');
INSERT INTO groupMembership (userID, gID, role) values(84, 6, 'user');
INSERT INTO groupMembership (userID, gID, role) values(94, 6, 'user');
INSERT INTO groupMembership (userID, gID, role) values(22, 6, 'user');
INSERT INTO groupMembership (userID, gID, role) values(83, 6, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(99, 7, 'user');
INSERT INTO groupMembership (userID, gID, role) values(12, 7, 'user');
INSERT INTO groupMembership (userID, gID, role) values(23, 7, 'user');
INSERT INTO groupMembership (userID, gID, role) values(33, 7, 'user');
INSERT INTO groupMembership (userID, gID, role) values(65, 7, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(39, 8, 'user');
INSERT INTO groupMembership (userID, gID, role) values(55, 8, 'user');
INSERT INTO groupMembership (userID, gID, role) values(66, 8, 'user');
INSERT INTO groupMembership (userID, gID, role) values(77, 8, 'user');
INSERT INTO groupMembership (userID, gID, role) values(59, 8, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(9, 9, 'user');
INSERT INTO groupMembership (userID, gID, role) values(69, 9, 'user');
INSERT INTO groupMembership (userID, gID, role) values(18, 9, 'user');
INSERT INTO groupMembership (userID, gID, role) values(17, 9, 'user');
INSERT INTO groupMembership (userID, gID, role) values(43, 9, 'manager');

INSERT INTO groupMembership (userID, gID, role) values(21, 10, 'user');
INSERT INTO groupMembership (userID, gID, role) values(31, 10, 'user');
INSERT INTO groupMembership (userID, gID, role) values(41, 10, 'user');
INSERT INTO groupMembership (userID, gID, role) values(51, 10, 'user');
INSERT INTO groupMembership (userID, gID, role) values(87, 10, 'manager');

--Insert some members pending membership into each group

INSERT INTO pendingGroupmembership values(1, 35, "hi");
INSERT INTO pendingGroupmembership values(2, 10, "hi");
INSERT INTO pendingGroupmembership values(3, 99, "hi");
INSERT INTO pendingGroupmembership values(4, 82, "hi");
INSERT INTO pendingGroupmembership values(5, 19, "hi");
INSERT INTO pendingGroupmembership values(6, 23, "hi");
INSERT INTO pendingGroupmembership values(7, 89, "hi");
INSERT INTO pendingGroupmembership values(8, 5, "hi");
INSERT INTO pendingGroupmembership values(9, 77, "hi");
INSERT INTO pendingGroupmembership values(10, 88, "hi");