package com.dreadblade.jlibo;

import com.dreadblade.jlibo.domain.Role;
import com.dreadblade.jlibo.domain.User;
import com.dreadblade.jlibo.repo.UserRepo;
import com.dreadblade.jlibo.service.MailService;
import com.dreadblade.jlibo.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailService mailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUser() throws Exception {
        User user = new User();

        user.setUsername("John");
        user.setPassword("password");
        user.setEmail("some@email.org");

        boolean isUserCreated = userService.addUser(user, null);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertNotEquals(user.getPassword(), "password");
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);

        Mockito.verify(mailService, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void addExistingUser() throws Exception {
        User user = new User();

        user.setUsername("John");
        user.setPassword("password");
        user.setEmail("some@email.org");

        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("John");

        boolean isUserCreated = userService.addUser(user, null);

        assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(user);

        Mockito.verify(mailService, Mockito.times(0))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }
}