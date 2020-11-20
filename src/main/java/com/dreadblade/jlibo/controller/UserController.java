package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Role;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam MultipartFile image,
            Model model
    ) throws IOException {
        if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            model.addAttribute("message", "Password confirmation cannot be empty");
            return "sign-up";
        }

        if (!passwordConfirmation.equals(user.getPassword())) {
            model.addAttribute("message", "Passwords are different!");
            return "sign-up";
        }

        if (!userService.addUser(user, image)) {
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

    @GetMapping("/user/{id}/edit")
    public String getUserEditPage(@PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);
        return "userEdit";
    }

    @PostMapping("/user/{id}/edit")
    public String updateUser(
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam boolean isActive,
            @RequestParam boolean isAdmin,
            @RequestParam MultipartFile image) throws IOException {
        User user = userService.findById(id);
        if (user != null) {
            user.setUsername(username);
            user.setActive(isActive);
            if (isAdmin) {
                user.getRoles().add(Role.ADMIN);
            }
            userService.updateUser(user, image);
            return "redirect:/users-list";
        }
        return "redirect:/user/" + id + "/edit";
    }

    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);

        return "user";
    }
}
