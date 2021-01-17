package com.dreadblade.jlibo.repo;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.domain.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepo extends JpaRepository<Book, Long> {

    @Query("select new com.dreadblade.jlibo.domain.dto.BookDto(" +
            "b, " +
            "count(bl), " +
            "sum(case when bl = :user then 1 else 0 end) > 0" +
            ") " +
            "from Book as b left join b.likes bl inner join Author as a " +
            "on b.author.id = a.id " +
            "WHERE lower(b.title) like concat('%', lower(:filter), '%') or " +
            "lower(a.name) like concat('%', lower(:filter), '%') and " +
            "b.isAccepted = true " +
            "group by b")
    Page<BookDto> findAllContainsFilter(Pageable pageable, @Param("user") User user, @Param("filter") String filter);

    @Query("select new com.dreadblade.jlibo.domain.dto.BookDto(" +
            "b, " +
            "count(bl), " +
            "sum(case when bl = :user then 1 else 0 end) > 0" +
            ") " +
            "from Book as b left join b.likes bl where b.isAccepted = true " +
            "group by b")
    Page<BookDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("select new com.dreadblade.jlibo.domain.dto.BookDto(" +
            "b, " +
            "count(bl), " +
            "sum(case when bl = :user then 1 else 0 end) > 0" +
            ") " +
            "from Book as b left join b.likes bl inner join Author as a " +
            "on b.author.id = a.id " +
            "WHERE (lower(b.title) like concat('%', lower(:filter), '%') or " +
            "lower(a.name) like concat('%', lower(:filter), '%')) and " +
            "a = :author and " +
            "b.isAccepted = true " +
            "group by b")
    Page<BookDto> findByAuthorContainsFilter(Pageable pageable, @Param("user") User user, @Param("author") Author author, String filter);

    @Query("select new com.dreadblade.jlibo.domain.dto.BookDto(" +
            "b, " +
            "count(bl), " +
            "sum(case when bl = :currentUser then 1 else 0 end) > 0" +
            ") " +
            "from Book as b left join b.likes bl inner join Author as a " +
            "on b.author.id = a.id " +
            "WHERE (lower(b.title) like concat('%', lower(:filter), '%') or " +
            "lower(a.name) like concat('%', lower(:filter), '%')) and " +
            "b.uploadedBy = :user and " +
            "b.isAccepted = true " +
            "group by b")
    Page<BookDto> findByUserContainsFilter(Pageable pageable, @Param("currentUser") User currentUser, @Param("user") User user, String filter);

    @Query("select new com.dreadblade.jlibo.domain.dto.BookDto(" +
            "b, " +
            "count(bl)" +
            ") " +
            "from Book as b left join b.likes bl where b.isAccepted = false " +
            "group by b")
    Page<BookDto> findAllNotAccepted(Pageable pageable);

    Book findByTitleAndAuthor(String title, Author author);
}