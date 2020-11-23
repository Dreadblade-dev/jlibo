insert into users (id, username, password, email, active) values (0, 'admin', 'admin', 'example@mail.com', true);

insert into user_role (user_id, roles) values (0, 'USER'), (0, 'ADMIN');