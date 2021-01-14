package com.dreadblade.jlibo.repo;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepo extends JpaRepository<Book, Long> {

    @Query("select b from Book as b inner join Author as a " +
            "on b.author.id = a.id " +
            "WHERE lower(b.title) like concat('%', lower(:filter), '%') or " +
            "lower(a.name) like concat('%', lower(:filter), '%') and " +
            "b.isAccepted = true")
    Page<Book> findAllContainsFilter(Pageable pageable, @Param("filter") String filter);

    @Query("select b from Book as b where b.isAccepted = true")
    Page<Book> findAll(Pageable pageable);

    @Query("select b from Book as b inner join Author as a " +
            "on b.author.id = a.id " +
            "WHERE (lower(b.title) like concat('%', lower(:filter), '%') or " +
            "lower(a.name) like concat('%', lower(:filter), '%')) and " +
            "a = :author and " +
            "b.isAccepted = true")
    Page<Book> findByAuthorContainsFilter(Pageable pageable, @Param("author") Author author, String filter);

    @Query("select b from Book as b inner join Author as a " +
            "on b.author.id = a.id " +
            "WHERE (lower(b.title) like concat('%', lower(:filter), '%') or " +
            "lower(a.name) like concat('%', lower(:filter), '%')) and " +
            "b.uploadedBy = :user and " +
            "b.isAccepted = true")
    Page<Book> findByUserContainsFilter(Pageable pageable, @Param("user") User user, String filter);

    @Query("select b from Book as b where b.isAccepted = false")
    Page<Book> findAllNotAccepted(Pageable pageable);

    Book findByTitleAndAuthor(String title, Author author);
}