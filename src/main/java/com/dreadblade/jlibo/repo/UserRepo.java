package com.dreadblade.jlibo.repo;

import com.dreadblade.jlibo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}