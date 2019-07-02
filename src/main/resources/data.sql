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
REPLACE INTO `instructor` VALUES (1, '1993-02-02', 'Fantastic trainer.', 'kjakubek@wp.pl', 'Katarzyna', 'Jakubek');
REPLACE INTO `instructor` VALUES (2, '1992-03-20', 'Experienced trainer.', 'mnowak@wp.pl', 'Marcin', 'Nowak');
REPLACE INTO `instructor` VALUES (3, '1992-03-20', 'Young, enthusiastic trainer.', 'klubaszenko@wp.pl', 'Karol', 'Lubaszenko');
REPLACE INTO `instructor` VALUES (4, '1985-11-24', 'New trainer at our gym. World wide famous.', 'robert@wp.pl', 'Robert', 'Burneika');
REPLACE INTO `instructor` VALUES (5, '1989-08-07', 'Weight lifting trainer.', 'marian@wp.pl', 'Marian', 'Krasnodorski');

-- insert training type
REPLACE INTO `training_type` VALUES (1, 'BodyPump is a barbell workout for anyone looking to get lean, toned and fit – fast.', 'LesMills BodyPump');
REPLACE INTO `training_type` VALUES (2, 'TRX is a form of suspension training that uses body weight exercises to develop strength, balance, flexibility and core stability simultaneously.', 'TRX');
REPLACE INTO `training_type` VALUES (3, 'A combination of traditional aerobic exercises with intensive interval training.', 'Tabata');
REPLACE INTO `training_type` VALUES (4, 'Classes that involve stretching exercises, which improve muscle flexibility and increase the range of motion at the joints.', 'Stretching');
REPLACE INTO `training_type` VALUES (5, 'This Body&Mind program is based on exercises performed at the pace of your own breathing and in full concentration.', 'Pilates');
REPLACE INTO `training_type` VALUES (6, 'Kettlebell is an effective way to shape your body and reduce fat by engaging many muscles at the same time.', 'Kettlebell');
REPLACE INTO `training_type` VALUES (7, 'Strengthening program, ideal for those who want to have strong and nicely‑defined muscles.', 'BodyShape');

-- insert training classes
REPLACE INTO `training_class` VALUES (1, '11-11', 55, 2, '2019-07-08T08:00:00', 1, 1);
REPLACE INTO `training_class` VALUES (2, '11-11', 55, 30, '2019-07-09T08:00:00', 1, 1);
REPLACE INTO `training_class` VALUES (3, '11-11', 55, 30, '2019-07-10T08:00:00', 1, 1);
REPLACE INTO `training_class` VALUES (4, '11-11', 55, 30, '2019-07-11T08:00:00', 1, 1);
REPLACE INTO `training_class` VALUES (5, '11-11', 55, 30, '2019-07-12T08:00:00', 1, 1);

REPLACE INTO `training_class` VALUES (6, '22-22', 55, 25, '2019-07-08T10:00:00', 2, 2);
REPLACE INTO `training_class` VALUES (7, '22-22', 55, 25, '2019-07-09T10:00:00', 2, 2);
REPLACE INTO `training_class` VALUES (8, '22-22', 55, 25, '2019-07-10T10:00:00', 2, 2);
REPLACE INTO `training_class` VALUES (9, '22-22', 55, 25, '2019-07-11T10:00:00', 2, 2);
REPLACE INTO `training_class` VALUES (10, '22-22', 55, 25, '2019-07-12T10:00:00', 2, 2);

REPLACE INTO `training_class` VALUES (11, '33-33', 55, 20, '2019-07-08T12:00:00', 4, 5);
REPLACE INTO `training_class` VALUES (12, '33-33', 55, 20, '2019-07-09T12:00:00', 4, 5);
REPLACE INTO `training_class` VALUES (13, '33-33', 55, 20, '2019-07-10T12:00:00', 4, 5);
REPLACE INTO `training_class` VALUES (14, '33-33', 55, 20, '2019-07-11T12:00:00', 4, 5);
REPLACE INTO `training_class` VALUES (15, '33-33', 55, 20, '2019-07-12T12:00:00', 4, 5);

REPLACE INTO `training_class` VALUES (16, '44-44', 55, 15, '2019-07-08T13:00:00', 4, null);
REPLACE INTO `training_class` VALUES (17, '44-44', 55, 15, '2019-07-09T13:00:00', 4, null);
REPLACE INTO `training_class` VALUES (18, '44-44', 55, 15, '2019-07-10T13:00:00', null, null);
REPLACE INTO `training_class` VALUES (19, '44-44', 55, 15, '2019-07-11T13:00:00', null, 5);
REPLACE INTO `training_class` VALUES (20, '44-44', 55, 15, '2019-07-12T13:00:00', null, 5);

REPLACE INTO `training_class` VALUES (21, '55-55', 55, 25, '2019-07-08T11:00:00', 5, 3);
REPLACE INTO `training_class` VALUES (22, '55-55', 55, 25, '2019-07-15T11:00:00', 5, 3);
REPLACE INTO `training_class` VALUES (23, '55-55', 55, 25, '2019-07-22T11:00:00', 5, 3);
REPLACE INTO `training_class` VALUES (24, '55-55', 55, 25, '2019-07-29T11:00:00', 5, 3);
REPLACE INTO `training_class` VALUES (25, '55-55', 55, 25, '2019-08-05T11:00:00', 5, 3);

REPLACE INTO `training_class` VALUES (26, '66-66', 55, 15, '2019-07-08T14:00:00', 3, 7);
REPLACE INTO `training_class` VALUES (27, '66-66', 55, 15, '2019-07-15T14:00:00', 3, 7);
REPLACE INTO `training_class` VALUES (28, '66-66', 55, 15, '2019-07-22T14:00:00', 3, 7);
REPLACE INTO `training_class` VALUES (29, '66-66', 55, 15, '2019-07-29T14:00:00', 3, 7);
REPLACE INTO `training_class` VALUES (30, '66-66', 55, 15, '2019-08-05T14:00:00', 3, 7);

REPLACE INTO `training_class` VALUES (31, '77-77', 55, 15, '2019-07-09T15:00:00', 4, 4);
REPLACE INTO `training_class` VALUES (32, '77-77', 55, 15, '2019-07-16T15:00:00', 4, 4);
REPLACE INTO `training_class` VALUES (33, '77-77', 55, 15, '2019-07-23T15:00:00', 4, 4);
REPLACE INTO `training_class` VALUES (34, '77-77', 55, 15, '2019-07-30T15:00:00', 4, 4);
REPLACE INTO `training_class` VALUES (35, '77-77', 55, 15, '2019-08-06T15:00:00', 4, 4);

REPLACE INTO `training_class` VALUES (36, '88-88', 55, 20, '2019-07-09T16:00:00', 3, 6);
REPLACE INTO `training_class` VALUES (37, '88-88', 55, 20, '2019-07-16T16:00:00', 3, 6);
REPLACE INTO `training_class` VALUES (38, '88-88', 55, 20, '2019-07-23T16:00:00', 3, 6);
REPLACE INTO `training_class` VALUES (39, '88-88', 55, 20, '2019-07-30T16:00:00', 3, 6);
REPLACE INTO `training_class` VALUES (40, '88-88', 55, 20, '2019-08-06T16:00:00', 3, 6);

REPLACE INTO `training_class` VALUES (41, '99-99', 55, 22, '2019-07-08T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (42, '99-99', 55, 22, '2019-07-09T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (43, '99-99', 55, 22, '2019-07-10T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (44, '99-99', 55, 22, '2019-07-11T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (45, '99-99', 55, 22, '2019-07-12T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (46, '99-99', 55, 22, '2019-07-13T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (47, '99-99', 55, 22, '2019-07-14T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (48, '99-99', 55, 22, '2019-07-15T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (49, '99-99', 55, 22, '2019-07-16T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (50, '99-99', 55, 22, '2019-07-17T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (51, '99-99', 55, 22, '2019-07-18T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (52, '99-99', 55, 22, '2019-07-19T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (53, '99-99', 55, 22, '2019-07-20T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (54, '99-99', 55, 22, '2019-07-21T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (55, '99-99', 55, 22, '2019-07-22T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (56, '99-99', 55, 22, '2019-07-23T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (57, '99-99', 55, 22, '2019-07-24T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (58, '99-99', 55, 22, '2019-07-25T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (59, '99-99', 55, 22, '2019-07-26T17:00:00', 5, 1);
REPLACE INTO `training_class` VALUES (60, '99-99', 55, 22, '2019-07-27T17:00:00', 5, 1);
