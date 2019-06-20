REPLACE INTO `role` VALUES (1,'ROLE_ADMIN');
REPLACE INTO `role` VALUES (2,'ROLE_USER');
REPLACE INTO `user` VALUES (1, 1, 'admin@wp.pl', 'Admin', 'Admin', '$2a$10$g/ChLwhxhXMEUhwqQwx7uu0VcYQQdIUpMUgBwqWvf2Zy1uBCLrVmS');
-- password for login: "admin@wp.pl" is: "12345"
REPLACE INTO `user_role` VALUES (1, 1);
REPLACE INTO `user_role` VALUES (1, 2);