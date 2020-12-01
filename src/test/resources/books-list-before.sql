delete from books;
delete from author;


insert into author(id, description, image_filename, name) values
(3, 'Author''s description', 'test/Leo tolstoy.jpg', 'Leo Tolstoy');

insert into books(id, book_filename, image_filename, title, author_id, user_id) values
(4, 'War and Peace.pdf', 'War and Peace.jpg', 'War and Peace', 3, 1),
(5, 'Childhood.pdf', 'Childhood.jpg', 'Childhood', 3, 1),
(6, 'The Prisoner of the Caucasus.pdf', 'The Prisoner of the Caucasus.jpg', 'The Prisoner of the Caucasus', 3, 1),
(7, 'Anna Karenina.pdf', 'Anna Karenina.jpg', 'Anna Karenina', 3, 1);

alter sequence hibernate_sequence restart with 10;