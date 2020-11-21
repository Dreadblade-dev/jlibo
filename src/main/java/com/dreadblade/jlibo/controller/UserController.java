package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.Role;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.service.UserService;
import com.dreadblade.jlibo.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.stream.Collectors;

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
            BindingResult bindingResult,
            @RequestParam(name = "password_confirmation") String passwordConfirmation,
            @RequestParam MultipartFile image,
            Model model
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getValidationErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            model.addAttribute("passwordConfirmIsInvalid", "Password confirmation cannot be empty");
        }

        if (passwordConfirmation != null && !passwordConfirmation.equals(user.getPassword())) {
            model.addAttribute("passwordIsInvalid", "Passwords are different!");
        }

        if (userService.isUserWithUsernameExists(user.getUsername())) {
            model.addAttribute("usernameIsInvalid", "User already exists!");
        }

        if (model.asMap().size() > 0) {
            model.addAttribute("user", user);
            return "sign-up";
        }

        userService.addUser(user, image);

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated!");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Activation code not found!");
            model.addAttribute("messageType", "danger");
        }

        return "login";
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
            @AuthenticationPrincipal User user,
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam(required = false) boolean isActive,
            @RequestParam(required = false) boolean isAdmin,
            @RequestParam MultipartFile image
    ) throws IOException {
        if (id.equals(user.getId()) || user.isAdmin()) {
            User userFromDb = userService.findById(id);
            if (userFromDb != null) {
                userFromDb.setUsername(username);
                if (user.isAdmin()) {
                    if (isAdmin) {
                        userFromDb.getRoles().add(Role.ADMIN);
                    } else {
                        userFromDb.getRoles().remove(Role.ADMIN);
                    }
                    userFromDb.setActive(isActive);
                }

                userService.updateUser(userFromDb, image);
                if (user.getId().equals(id)) {
                    return "redirect:/user/" + id;
                }
                if (user.isAdmin()) {
                    return "redirect:/users-list";
                }
            }
            return "redirect:/user/" + id + "/edit";
        }
        return "redirect:/user/" + id;
    }

    @GetMapping("/user/{id}")
    public String getUserPage(@RequestParam(required = false, defaultValue = "") String filter,
                              @PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("books", user.getUploadedBooks());
        return "user";
    }

    @GetMapping("/user/{id}/filter")
    public String getUserPageWithFilter(@RequestParam(required = false, defaultValue = "") String filter,
                                        @PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);

        List<Book> books = user.getUploadedBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                        b.getAuthor().getName().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());

        model.addAttribute("books", books);
        model.addAttribute("filter", filter);
        return "user";
    }
}
