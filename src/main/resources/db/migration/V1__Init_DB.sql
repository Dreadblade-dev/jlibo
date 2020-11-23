create sequence hibernate_sequence start 1 increment 1;

create table author (
    id int8 not null,
    description varchar(2048),
    image_filename varchar(255),
    name varchar(128),
    primary key (id)
);

create table books (
    id int8 not null,
    book_filename varchar(255),
    image_filename varchar(255),
    title varchar(128),
    author_id int8,
    user_id int8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table users (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255) unique,
    image_filename varchar(255),
    password varchar(128),
    username varchar(32) unique,
    primary key (id)
);

alter table if exists books
    add constraint book_author_fk
    foreign key (author_id) references author;

alter table if exists books
    add constraint book_user_fk
    foreign key (user_id) references users;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references users;