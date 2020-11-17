package com.dreadblade.jlibo.repo;

import com.dreadblade.jlibo.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Long> {
    Author findByName(String name);
}
