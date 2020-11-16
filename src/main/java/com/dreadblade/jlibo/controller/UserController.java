package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(name = "password_confirmation") String passwordConfirmation
    ) {
        userService.addUser(username, password, passwordConfirmation);

        return "redirect:/login";
    }
}
