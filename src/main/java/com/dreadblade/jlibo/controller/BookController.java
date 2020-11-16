package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;

    @Value("${jlibo.upload.path}")
    private String uploadPath;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String getMainPage(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "main";
    }

    @GetMapping(value = "/books/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getBookFile(@PathVariable("id") Book book) {
        try {
            String filename = uploadPath + "/books/" + book.getBookFilename();
            FileInputStream fis = new FileInputStream(filename);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException when user downloads file");
        }
    }

    @PostMapping("/books")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam("image") MultipartFile image,
                          @RequestParam("book") MultipartFile book,
                          Model model
    ) throws IOException {
        bookService.addBook(title, author, image, book);
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "main";
    }
}