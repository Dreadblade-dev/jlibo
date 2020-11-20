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
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    @Value("${jlibo.upload.path}")
    private String uploadPath;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
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
        user.setActive(true);

        if (image != null && !image.isEmpty()) {
            String filename = FileUtil.saveFile(image, uploadPath, FileUtil.TypeOfFile.USER_IMAGE);
            user.setImageFilename(filename);
        }

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
        userRepo.save(user);
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseGet(null);
    }
}