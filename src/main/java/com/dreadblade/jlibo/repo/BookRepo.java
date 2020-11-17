package com.dreadblade.jlibo.repo;

import com.dreadblade.jlibo.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
    Book findByTitleAndAuthorName(String title, String authorName);
}
