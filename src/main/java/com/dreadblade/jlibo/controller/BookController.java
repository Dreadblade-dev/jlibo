package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.service.BookService;
import com.dreadblade.jlibo.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/filter")
    public String getMainPageWithFilter(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        List<Book> books = bookService.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                        b.getAuthor().getName().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());

        model.addAttribute("books", books);
        model.addAttribute("filter", filter);
        return "main";
    }

    @GetMapping(value = "/book/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getBookFile(@PathVariable("id") Book book) {
        try {
            String filename = uploadPath + "/books/books/" + book.getBookFilename();
            FileInputStream fis = new FileInputStream(filename);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException when user downloads file");
        }
    }

    @PostMapping("/book/new")
    public String addBook(@Valid Book book,
                          BindingResult bindingResult,
                          @RequestParam("author_id") Author author,
                          @RequestParam MultipartFile imageFile,
                          @RequestParam MultipartFile bookFile,
                          @AuthenticationPrincipal User uploadedBy,
                          Model model
    ) throws IOException {
        if (bindingResult.hasErrors() || imageFile == null || imageFile.isEmpty() ||
                bookFile == null || bookFile.isEmpty()) {
            Map<String, String> errors = ControllerUtils.getValidationErrors(bindingResult);
            model.mergeAttributes(errors);

            if (imageFile == null || imageFile.isEmpty()) {
                model.addAttribute("imageFilenameIsInvalid", "Book's cover image cannot be empty");
            }
            if (bookFile == null || bookFile.isEmpty()) {
                model.addAttribute("bookFilenameIsInvalid", "Book file cannot be empty");
            }

            model.addAttribute("title", book.getTitle());
            model.addAttribute("author", author);
            model.addAttribute("books", author.getBooks());
            return "author";
        }

        book.setAuthor(author);
        boolean isBookAdded = bookService.addBook(book, imageFile, bookFile, uploadedBy);

        if (!isBookAdded) {
            model.addAttribute("message", "This book already exists!");
        }

        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "redirect:/author/" + author.getId();
    }

    @GetMapping("/book/{id}/edit")
    public String getBookEditPage(@PathVariable("id") Book book, Model model) {
        model.addAttribute("book", book);
        return "bookEdit";
    }

    @PostMapping("/book/{id}/edit")
    public String updateBook(@Valid Book book, BindingResult bindingResult,
                             @RequestParam("author_id") Author author,
                             @RequestParam MultipartFile imageFile,
                             @RequestParam MultipartFile bookFile,
                             Model model) throws IOException {
        Book temp = bookService.findById(book.getId());

        if (temp == null) {
            return "redirect:/author/" + author.getId();
        }
        int errorsCount = 0;
        book.setAuthor(author);
        book.setUploadedBy(temp.getUploadedBy());

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getValidationErrors(bindingResult);
            model.mergeAttributes(errors);

            errorsCount += errors.size();
        }

        if (imageFile == null || imageFile.isEmpty()) {
            if (temp.getImageFilename() == null || temp.getImageFilename().isEmpty()) {
                model.addAttribute("imageFilenameIsInvalid", "Book's cover image cannot be empty");
                errorsCount++;
            } else {
                book.setImageFilename(temp.getImageFilename());
            }
        }
        if (bookFile == null || bookFile.isEmpty()) {
            if (temp.getBookFilename() == null || temp.getBookFilename().isEmpty()) {
                model.addAttribute("bookFilenameIsInvalid", "Book file cannot be empty");
                errorsCount++;
            } else {
                book.setBookFilename(temp.getBookFilename());
            }
        }

        if (errorsCount > 0) {
            model.addAttribute("book", book);
            return "bookEdit";
        }

        bookService.updateBook(book, imageFile, bookFile);

        return "redirect:/author/" + author.getId();
    }

    @PostMapping("/book/{id}/delete")
    public String deleteBook(@PathVariable("id") Book book) {
        if (book != null) {
            bookService.deleteBookById(book.getId());
            return "redirect:/author/" + book.getAuthor().getId();
        }

        return "redirect:/";
    }
}