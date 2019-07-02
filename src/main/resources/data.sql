-- insert roles
REPLACE INTO `role` VALUES (1,'ROLE_SUPER');
REPLACE INTO `role` VALUES (2,'ROLE_ADMIN');
REPLACE INTO `role` VALUES (3,'ROLE_USER');

-- insert superadmin user
REPLACE INTO `user` VALUES (1, 1, 'super@javagym.pl', 'Admin', 'Admin', false ,'$2a$10$g/ChLwhxhXMEUhwqQwx7uu0VcYQQdIUpMUgBwqWvf2Zy1uBCLrVmS');
-- -- password for login: "superadmin@javagym.pl" is: "12345"
REPLACE INTO `user_role` VALUES (1, 1);
REPLACE INTO `user_role` VALUES (1, 2);
REPLACE INTO `user_role` VALUES (1, 3);

-- insert admin user
REPLACE INTO `user` VALUES (2, 1, 'admin@javagym.pl', 'Admin', 'Admin', false ,'$2a$10$g/ChLwhxhXMEUhwqQwx7uu0VcYQQdIUpMUgBwqWvf2Zy1uBCLrVmS');
-- -- password for login: "admin@javagym.pl" is: "12345"
REPLACE INTO `user_role` VALUES (2, 2);
REPLACE INTO `user_role` VALUES (2, 3);

-- insert regular user
REPLACE INTO `user` VALUES (3, 1, 'javagymtest1@gmail.com', 'Adam', 'Małysz', true ,'$2a$10$G9tN3.tOA788nw1HyCyl9ui71y57MgP8w/rQz15Zo3BR7Gg5tSeq2');
-- -- password for login: "javagymtest1@gmail.com" is: "12345"
REPLACE INTO `user_role` VALUES (3, 3);

REPLACE INTO `user` VALUES (4, 1, 'javagymtest2@gmail.com', 'Anna', 'Mucha', true ,'$2a$10$tcsaqZgdcyMD5VGqHN5gY.mJJXS7r/FFfg81Chsaoy5YKTKQCP4jq');
-- -- password for login: "javagymtest2@gmail.com" is: "123123"
REPLACE INTO `user_role` VALUES (4, 3);

REPLACE INTO `user` VALUES (5, 1, 'javagymtest3@gmail.com', 'Krzysztof', 'Jarzyna', true ,'$2a$10$tcsaqZgdcyMD5VGqHN5gY.mJJXS7r/FFfg81Chsaoy5YKTKQCP4jq');
-- -- password for login: "javagymtest3@gmail.com" is: "123123"
REPLACE INTO `user_role` VALUES (5, 3);

-- insert instructor
INSERT INTO instructor (id, date_of_birth, description, email, first_name, last_name) VALUES (1, '1993-02-02', 'Fantastic trainer.', 'kjakubek@wp.pl', 'Katarzyna', 'Jakubek');
INSERT INTO instructor (id, date_of_birth, description, email, first_name, last_name) VALUES (2, '1992-03-20', 'Experienced trainer', 'mnowak@wp.pl', 'Marcin', 'Nowak');
INSERT INTO instructor (id, date_of_birth, description, email, first_name, last_name) VALUES (3, '1992-03-20', 'Young, enthusiastic trainer', 'klubaszenko@wp.pl', 'Karol', 'Lubaszenko');

-- insert training type
INSERT INTO training_type (id, description, name) VALUES (1, 'BodyPump is a barbell workout for anyone looking to get lean, toned and fit – fast.', 'LesMills BodyPump');
INSERT INTO training_type (id, description, name) VALUES (2, 'TRX is a form of suspension training that uses body weight exercises to develop strength, balance, flexibility and core stability simultaneously.', 'TRX');
INSERT INTO training_type (id, description, name) VALUES (3, 'A combination of traditional aerobic exercises with intensive interval training.', 'Tabata');
INSERT INTO training_type (id, description, name) VALUES (4, 'Classes that involve stretching exercises, which improve muscle flexibility and increase the range of motion at the joints.', 'Stretching');
INSERT INTO training_type (id, description, name) VALUES (5, 'This Body&Mind program is based on exercises performed at the pace of your own breathing and in full concentration.', 'Pilates');
INSERT INTO training_type (id, description, name) VALUES (6, 'Kettlebell is an effective way to shape your body and reduce fat by engaging many muscles at the same time.', 'Kettlebell');
INSERT INTO training_type (id, description, name) VALUES (7, 'Strengthening program, ideal for those who want to have strong and nicely‑defined muscles.', 'BodyShape');
