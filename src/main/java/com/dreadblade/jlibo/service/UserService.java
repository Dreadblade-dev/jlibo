package com.dreadblade.jlibo.service;

import com.dreadblade.jlibo.domain.Role;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addUser(String username, String password, String passwordConfirmation) {
        User userFromDb = userRepo.findByUsername(username);

        if (userFromDb == null) {
            if (password != null && password.equals(passwordConfirmation)) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setRoles(Collections.singleton(Role.USER));
                user.setActive(true);

                userRepo.save(user);
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}