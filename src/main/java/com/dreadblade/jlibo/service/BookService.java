package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepo bookRepo;

    @Value("${jlibo.upload.path}")
    private String uploadPath;

    @Autowired
    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public void addBook(String title, String author, MultipartFile imageFile, MultipartFile bookFile) throws IOException {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        saveFile(imageFile, book);
        saveFile(bookFile, book);
        bookRepo.save(book);
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
