package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String getSignUpPage() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(
            @Valid User user,
            @RequestParam(name = "password_confirmation") String passwordConfirmation,
            Model model
    ) {
        if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            model.addAttribute("message", "Password confirmation cannot be empty");
            return "sign-up";
        }

        if (!passwordConfirmation.equals(user.getPassword())) {
            model.addAttribute("message", "Passwords are different!");
            return "sign-up";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("message", "User already exists!");
            return "sign-up";
        }

        return "redirect:/login";
    }

    @GetMapping("/users-list")
    public String usersList(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "usersList";
    }
}
