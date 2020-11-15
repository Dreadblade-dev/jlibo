package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.service.BookService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @GetMapping("/books/{id}")
    public void getBookFile(@PathVariable("id") Book book, HttpServletResponse response) {
        try {
            String filename = uploadPath + "/books/" + book.getBookFilename();
            InputStream is = new FileInputStream(filename);
            IOUtils.copy(is, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
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
