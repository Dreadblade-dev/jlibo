delete from user_role;
delete from users;

insert into users(id, active, password, username) values
(1, true, '$2a$08$m1cJ8xgyF/rv.kOUyeZI9ulKy3Jgv64zbXe1cSwy0YFWNVPEpyvbq', 'anton'), /* password=password */
(2, true, '$2a$08$m1cJ8xgyF/rv.kOUyeZI9ulKy3Jgv64zbXe1cSwy0YFWNVPEpyvbq', 'user'); /* password=password */

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');