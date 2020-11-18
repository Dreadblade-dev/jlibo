package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.repo.BookRepo;
import com.dreadblade.jlibo.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookService {
    private final BookRepo bookRepo;
    private final AuthorService authorService;

    @Value("${jlibo.upload.path}")
    private String uploadPath;

    @Autowired
    public BookService(BookRepo bookRepo, AuthorService authorService) {
        this.bookRepo = bookRepo;
        this.authorService = authorService;
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
        Author author = authorService.findByName(authorName);

        if (author == null) {
            authorService.addAuthor(authorName);
            author = authorService.findByName(authorName);
        }

        book.setTitle(title);
        book.setAuthor(author);
        author.getBooks().add(book);
        book.setUploadedBy(uploadedBy);

        FileUtil.saveBookFiles(imageFile, book, uploadPath);
        FileUtil.saveBookFiles(bookFile, book, uploadPath);

        bookRepo.save(book);
        return true;
    }
}
