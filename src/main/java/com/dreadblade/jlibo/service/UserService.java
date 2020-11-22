package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Role;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.repo.UserRepo;
import com.dreadblade.jlibo.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final MailService mailService;

    @Value("${jlibo.upload.path}")
    private String uploadPath;

    @Value("${jlibo.host.name}")
    private String hostname;

    @Autowired
    public UserService(UserRepo userRepo, MailService mailService) {
        this.userRepo = userRepo;
        this.mailService = mailService;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public boolean addUser(User user, MultipartFile image) throws IOException {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setUploadedBooks(new HashSet<>());

        if (image != null && !image.isEmpty()) {
            String filename = FileUtil.saveFile(image, uploadPath, FileUtil.TypeOfFile.USER_IMAGE);
            user.setImageFilename(filename);
        }

        user.setActive(false);
        sendMessage(user);

        userRepo.save(user);
        return true;
    }

    private void sendMessage(User user) {
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            String message = String.format("Hello, %s!\n" +
                    "Welcome to Jlibo. To activate your account, please, follow the link below: %s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailService.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }
        user.setActive(true);

        userRepo.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public void updateUser(User user, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String filename = FileUtil.saveFile(image, uploadPath, FileUtil.TypeOfFile.USER_IMAGE);
            user.setImageFilename(filename);
        }
        User temp = userRepo.findById(user.getId()).orElse(null);
        if (temp != null) {
            user.setUploadedBooks(temp.getUploadedBooks());
        }
        userRepo.save(user);
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseGet(null);
    }

    public boolean isUserWithUsernameExists(String username) {
        if (userRepo.findByUsername(username) != null) {
            return true;
        }
        return false;
    }

    public boolean isUserWithEmailExists(String email) {
        if (userRepo.findByEmail(email) != null) {
            return true;
        }
        return false;
    }
}