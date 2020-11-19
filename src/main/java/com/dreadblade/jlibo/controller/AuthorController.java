package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Author;
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

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author/{id}")
    public String getAuthorPage(@PathVariable("id") Author author, Model model) {
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
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