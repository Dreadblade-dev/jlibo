create table book_likes (
    book_id int8 not null references books,
    user_id int8 not null references users,
    primary key (book_id, user_id)
);