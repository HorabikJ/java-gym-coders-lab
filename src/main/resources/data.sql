
-- insert roles
REPLACE INTO `role` VALUES (1,'ROLE_ADMIN');
REPLACE INTO `role` VALUES (2,'ROLE_USER');

-- insert admin user
REPLACE INTO `user` VALUES (1, 1, 'admin@wp.pl', 'Admin', 'Admin', false ,'$2a$10$g/ChLwhxhXMEUhwqQwx7uu0VcYQQdIUpMUgBwqWvf2Zy1uBCLrVmS');
-- -- password for login: "admin@wp.pl" is: "12345"
REPLACE INTO `user_role` VALUES (1, 1);
REPLACE INTO `user_role` VALUES (1, 2);


-- insert regular active user
REPLACE INTO `user` VALUES (2, 1, 'jacek@wp.pl', 'Jacek', 'Horabik', true ,'$2a$10$G9tN3.tOA788nw1HyCyl9ui71y57MgP8w/rQz15Zo3BR7Gg5tSeq2');
-- -- password for login: "jacek@wp.pl" is: "12345"
REPLACE INTO `user_role` VALUES (2, 2);

REPLACE INTO `user` VALUES (3, 1, 'marcin@wp.pl', 'Marcin', 'Kowalski', true ,'$2a$10$tcsaqZgdcyMD5VGqHN5gY.mJJXS7r/FFfg81Chsaoy5YKTKQCP4jq');
-- -- password for login: "jacek@wp.pl" is: "123123"
REPLACE INTO `user_role` VALUES (3, 2);

-- insert instructor
INSERT INTO instructor (id, date_of_birth, description, email, first_name, last_name) VALUES (1, '1993-02-02', 'Fantastic trainer', 'trainer@wp.pl', 'Katarzyna', 'Jakubek');

-- insert training type
INSERT INTO training_type (id, description, name) VALUES (1, 'Workout with bars and heavy lifting.', 'LesMills BodyPump');
INSERT INTO training_type (id, description, name) VALUES (2, 'Intense workout with music.', 'BodyShape');
