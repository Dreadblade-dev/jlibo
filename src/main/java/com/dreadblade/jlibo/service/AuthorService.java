package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.repo.AuthorRepo;
import com.dreadblade.jlibo.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Service
public class AuthorService {
    @Value("${jlibo.upload.path}")
    private String uploadPath;
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

    public Author findById(Long id) {
        return authorRepo.findById(id).orElseGet(null);
    }

    public boolean addAuthor(String name) {
        Author authorFromDb = authorRepo.findByName(name);

        if (authorFromDb != null) {
            return false;
        }

        Author author = new Author();
        author.setName(name);
        author.setDescription("<" + name + "\'s biography>");
        author.setBooks(new HashSet<>());

        authorRepo.save(author);

        return true;
    }

    public void updateAuthor(Author author, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageFilename = FileUtil.saveFile(image, uploadPath, FileUtil.TypeOfFile.AUTHOR_IMAGE);
            author.setImageFilename(imageFilename);
        }
        authorRepo.save(author);
    }
}
