package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.repo.AuthorRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepo authorRepo;

    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<Author> findAll() {
        return authorRepo.findAll();
    }

    public Author findByName(String authorName) {
        return authorRepo.findByName(authorName);
    }

    public boolean addAuthor(String name) {
        Author authorFromDb = authorRepo.findByName(name);

        if (authorFromDb != null) {
            return false;
        }

        Author author = new Author();
        author.setName(name);
        author.setBooks(new HashSet<>());

        authorRepo.save(author);

        return true;
    }
}
