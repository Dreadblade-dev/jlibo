package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Author;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AuthorController {
    @GetMapping("/author/{id}")
    public String getAuthorPage(@PathVariable("id") Author author, Model model) {
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
        return "author";
    }
}
