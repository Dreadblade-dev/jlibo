package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.service.AuthorService;
import com.dreadblade.jlibo.service.BookService;
import com.dreadblade.jlibo.util.ControllerUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("/author/{id}")
    public String getAuthorPage(
            @PathVariable("id") Author author,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model
    ) {
        Page<Book> page = bookService.findByAuthor(pageable, author, filter);

        model.addAttribute("filter", filter);
        model.addAttribute("page", page);
        if (filter == null || filter.isEmpty()) {
            model.addAttribute("url", "/author/" + author.getId());
        } else {
            model.addAttribute("url", "/author/" + author.getId() + "?filter=" + filter);
        }

        model.addAttribute("author", author);
        return "author";
    }
    @GetMapping("/author/new")
    public String getAddAuthorPage() {
        return "authorEdit";
    }

    @PostMapping("/author/new")
    public String addAuthor(@Valid Author author, BindingResult bindingResult,
                            @RequestParam MultipartFile image, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getValidationErrors(bindingResult);
            model.mergeAttributes(errors);

            model.addAttribute("author", author);

            return "authorEdit";
        }

        authorService.addAuthor(author, image);

        author = authorService.findByName(author.getName());

        return "redirect:/author/" + author.getId();
    }

    @GetMapping("/author/{id}/edit")
    public String getAuthorEditPage(@PathVariable("id") Long id, Model model) {
        Author author = authorService.findById(id);

        if (author == null) {
            return "redirect:/";
        }

        model.addAttribute("author", author);
        return "authorEdit";
    }

    @PostMapping("/author/{id}/edit")
    public String updateAuthor(@Valid Author author, BindingResult bindingResult,
                               @RequestParam MultipartFile image, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getValidationErrors(bindingResult);
            model.mergeAttributes(errors);
            return "authorEdit";
        }

        authorService.updateAuthor(author, image);
        return "redirect:/author/" + author.getId();
    }

    @PostMapping("/author/{id}/delete")
    public String deleteAuthor(@PathVariable("id") Author author) {
        if (author != null) {
            authorService.deleteById(author.getId());
        }

        return "redirect:/";
    }

    @GetMapping("/authors-list")
    public String getAuthorsList(Model model) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        return "authorsList";
    }
}