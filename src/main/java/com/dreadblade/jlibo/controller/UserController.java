package com.dreadblade.jlibo.controller;

import com.dreadblade.jlibo.config.RecaptchaConfig;
import com.dreadblade.jlibo.domain.Book;
import com.dreadblade.jlibo.domain.Role;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.domain.dto.RecaptchaResponseDto;
import com.dreadblade.jlibo.service.BookService;
import com.dreadblade.jlibo.service.UserService;
import com.dreadblade.jlibo.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    private final UserService userService;
    private final BookService bookService;
    private final RecaptchaConfig recaptchaConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public UserController(UserService userService, BookService bookService, RecaptchaConfig recaptchaConfig, RestTemplate restTemplate) {
        this.userService = userService;
        this.bookService = bookService;
        this.recaptchaConfig = recaptchaConfig;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/sign-up")
    public String getSignUpPage(Model model) {
        model.addAttribute("recaptcha", recaptchaConfig);
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(
            @Valid User user,
            BindingResult bindingResult,
            @RequestParam(name = "password_confirmation") String passwordConfirmation,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse,
            @RequestParam MultipartFile image,
            Model model
    ) throws IOException {
        int errorsCount = 0;

        String url = String.format(RECAPTCHA_URL, recaptchaConfig.getSecretKey(), recaptchaResponse);
        RecaptchaResponseDto response = restTemplate.postForObject(
                url, Collections.emptyList(), RecaptchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("recaptchaError", "Recaptcha is invalid");
            errorsCount++;
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getValidationErrors(bindingResult);
            model.mergeAttributes(errors);

            errorsCount += errors.size();
        }

        if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            model.addAttribute("passwordConfirmIsInvalid", "Password confirmation cannot be empty");
            errorsCount++;
        }

        if (passwordConfirmation != null && !passwordConfirmation.equals(user.getPassword())) {
            model.addAttribute("passwordIsInvalid", "Passwords are different!");
            errorsCount++;
        }

        if (userService.isUserWithEmailExists(user.getEmail())) {
            model.addAttribute("emailIsInvalid", "This email already in use!");
            errorsCount++;
        }

        if (userService.isUserWithUsernameExists(user.getUsername())) {
            model.addAttribute("usernameIsInvalid", "User already exists!");
            errorsCount++;
        }

        if (errorsCount > 0) {
            model.addAttribute("user", user);
            model.addAttribute("recaptcha", recaptchaConfig);
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

        model.addAttribute("recaptcha", recaptchaConfig);

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
    public String getUserPage(
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String filter,
            @PathVariable("id") User user, Model model
    ) {
        Page<Book> page = bookService.findByUser(pageable, user, filter);

        model.addAttribute("user", user);
        model.addAttribute("filter", filter);
        model.addAttribute("page", page);
        if (filter == null || filter.isEmpty()) {
            model.addAttribute("url", "/user/" + user.getId());
        } else {
            model.addAttribute("url", "/user/" + user.getId() + "?filter=" + filter);
        }
        return "user";
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") User u,
                             @AuthenticationPrincipal User user) {
        userService.deleteById(u.getId());
        if (user.isAdmin() && !user.getId().equals(u.getId())) {
            return "redirect:/users-list";
        }
        return "redirect:/";
    }
}
