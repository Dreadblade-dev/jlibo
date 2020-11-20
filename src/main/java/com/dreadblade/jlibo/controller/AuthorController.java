package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Author;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author/{id}")
    public String getAuthorPage(@RequestParam(required = false, defaultValue = "") String filter,
                                @PathVariable("id") Author author, Model model) {
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
        return "author";
    }

    @GetMapping("/author/{id}/filter")
    public String getAuthorPageWithFilter(@RequestParam(required = false, defaultValue = "") String filter,
                                        @PathVariable("id") Author author, Model model) {
        model.addAttribute("author", author);

        List<Book> books = author.getBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                        b.getAuthor().getName().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());

        model.addAttribute("books", books);
        model.addAttribute("filter", filter);
        return "author";
    }

    @GetMapping("/author/new")
    public String addAuthor(Model model) {
        authorService.addAuthor("<author's name>");

        Author author = authorService.findByName("<author's name>");

        model.addAttribute("author", author);
        return "authorEdit";
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
    public String updateAuthor(@Valid Author author, @RequestParam MultipartFile image) throws IOException {
        authorService.updateAuthor(author, image);
        return "redirect:/author/" + author.getId();
    }
}