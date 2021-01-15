package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.repo.BookRepo;
import com.dreadblade.jlibo.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    public Page<Book> findAll(Pageable pageable, String filter) {
        if (filter == null || filter.isEmpty()) {
            return bookRepo.findAll(pageable);
        }

        return bookRepo.findAllContainsFilter(pageable, filter);
    }

    public Book findById(Long id) {
        return bookRepo.findById(id).orElse(null);
    }

    public Page<Book> findByAuthor(Pageable pageable, Author author, String filter) {
        return bookRepo.findByAuthorContainsFilter(pageable, author, filter);
    }

    public Page<Book> findByUser(Pageable pageable, User user, String filter) {
        return bookRepo.findByUserContainsFilter(pageable, user, filter);
    }

    public Page<Book> findAllNotAccepted(Pageable pageable) {
        return bookRepo.findAllNotAccepted(pageable);
    }

    public boolean addBook(Book book, MultipartFile imageFile,
                        MultipartFile bookFile, User uploadedBy) throws IOException {

        Book bookFromDb = bookRepo.findByTitleAndAuthor(book.getTitle(), book.getAuthor());

        if (bookFromDb != null) {
            return false;
        }

        book.getAuthor().getBooks().add(book);
        book.setUploadedBy(uploadedBy);

        String bookFilename = FileUtil.saveFile(bookFile, uploadPath, FileUtil.TypeOfFile.BOOK_FILE);
        book.setBookFilename(bookFilename);

        String imageFilename = FileUtil.saveFile(imageFile, uploadPath, FileUtil.TypeOfFile.BOOK_IMAGE);
        book.setImageFilename(imageFilename);

        if (uploadedBy.isAdmin()) {
            book.setAccepted(true);
        }

        bookRepo.save(book);
        return true;
    }

    public boolean acceptBook(Book book) {
        if (book.isAccepted()) {
            return true;
        }

        book.setAccepted(true);

        bookRepo.save(book);
        return true;
    }

    public boolean declineBook(Book book) {
        if (!book.isAccepted()) {
            deleteBookById(book.getId());
            return true;
        }

        return false;
    }

    public boolean isBookAccepted(Book book) {
        return bookRepo.findByTitleAndAuthor(book.getTitle(), book.getAuthor()).isAccepted();
    }

    public void updateBook(Book book, MultipartFile imageFile, MultipartFile bookFile) throws IOException {
        if (bookFile != null && !bookFile.isEmpty()) {
            String bookFilename = FileUtil.saveFile(bookFile, uploadPath, FileUtil.TypeOfFile.BOOK_FILE);
            book.setBookFilename(bookFilename);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFilename = FileUtil.saveFile(imageFile, uploadPath, FileUtil.TypeOfFile.BOOK_IMAGE);
            book.setImageFilename(imageFilename);
        }

        bookRepo.save(book);
    }

    public void deleteBookById(Long id) {
        if (bookRepo.findById(id).orElse(null) != null) {
            bookRepo.deleteById(id);
        }
    }
}