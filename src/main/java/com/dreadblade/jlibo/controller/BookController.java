package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.domain.dto.BookDto;
import com.dreadblade.jlibo.service.BookService;
import com.dreadblade.jlibo.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
    public String getMainPage(@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(required = false, defaultValue = "") String filter,
                              @AuthenticationPrincipal User currentUser,
                              Model model) {
        Page<BookDto> page = bookService.findAll(pageable, currentUser, filter);

        model.addAttribute("filter", filter);
        model.addAttribute("page", page);
        if (filter == null || filter.isEmpty()) {
            model.addAttribute("url", "/");
        } else {
            model.addAttribute("url", "/?filter=" + filter);
        }

        return "main";
    }

    @GetMapping("/book/{id}/edit")
    public String getBookEditPage(@PathVariable("id") Book book, Model model) {
        model.addAttribute("book", book);
        model.addAttribute("isAccepted", bookService.isBookAccepted(book));
        return "bookEdit";
    }

    @GetMapping("/book/suggested")
    public String getSuggestedBooksPage(@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                                    Model model) {
        Page<BookDto> page = bookService.findAllNotAccepted(pageable);

        model.addAttribute("page", page);
        model.addAttribute("isSuggestedPage", true);
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
    public String addBook(@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                          @Valid Book book,
                          BindingResult bindingResult,
                          @RequestParam("author_id") Author author,
                          @RequestParam MultipartFile imageFile,
                          @RequestParam MultipartFile bookFile,
                          @AuthenticationPrincipal User uploadedBy,
                          @RequestParam(required = false, defaultValue = "") String filter,
                          Model model
    ) throws IOException {
        Page<BookDto> page = bookService.findAll(pageable, uploadedBy, filter);
        model.addAttribute("page", page);

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
            model.addAttribute("message", "Your book is not uploaded! Check the uploading form for details!");

            return "author";
        }

        book.setAuthor(author);
        boolean isBookAdded = bookService.addBook(book, imageFile, bookFile, uploadedBy);

        if (!isBookAdded) {
            model.addAttribute("message", "This book already exists!");
            model.addAttribute("author", author);
            return "author";
        }

        return "redirect:/author/" + author.getId() + "?upload=true";
    }

    @GetMapping("/book/{id}/like")
    public String addBookToCollection(@AuthenticationPrincipal User user,
                                      @PathVariable("id") Book book,
                                      RedirectAttributes redirectAttributes,
                                      @RequestHeader(required = false) String referer) {
        Set<User> likes = book.getLikes();

        if (likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }

    @PostMapping("/book/{id}/accept")
    public String acceptBook(@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                             @PathVariable("id") Book book,
                             RedirectAttributes redirectAttributes,
                             @RequestHeader(required = false) String referer) {
        bookService.acceptBook(book);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }

    @PostMapping("/book/{id}/decline")
    public String declineBook(@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                              @PathVariable("id") Book book,
                              RedirectAttributes redirectAttributes,
                              @RequestHeader(required = false) String referer) {
        bookService.declineBook(book);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
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