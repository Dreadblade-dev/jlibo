package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.repo.AuthorRepo;
import com.dreadblade.jlibo.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;

    @Value("${jlibo.upload.path}")
    private String uploadPath;

    @Autowired
    public BookService(BookRepo bookRepo, AuthorRepo authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public boolean addBook(String title, String authorName, MultipartFile imageFile,
                        MultipartFile bookFile, User uploadedBy) throws IOException {

        Book bookFromDb = bookRepo.findByTitleAndAuthorName(title, authorName);

        if (bookFromDb != null) {
            return false;
        }

        Book book = new Book();
        book.setTitle(title);

        Author author = authorRepo.findByName(authorName);

        if (author == null) {
            author = new Author();
            author.setName(authorName);
            author.setBooks(new HashSet<>());
        }

        book.setAuthor(author);
        author.getBooks().add(book);
        book.setUploadedBy(uploadedBy);

        saveFile(imageFile, book);
        saveFile(bookFile, book);

        authorRepo.save(author);
        bookRepo.save(book);
        return true;
    }

    private void saveFile(MultipartFile file, Book book) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String fileDirectory = file.getOriginalFilename().endsWith(".pdf") ? "/books" : "/images";
            File uploadDir = new File(uploadPath + fileDirectory);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String resultFilename = uuid + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadDir.toString() + "/" + resultFilename));

            if (file.getOriginalFilename().endsWith(".pdf")) {
                book.setBookFilename(resultFilename);
            } else {
                book.setImageFilename(resultFilename);
            }
        }
    }
}
