package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username
    User findByEmail(String email
    boolean existsByUsername(String username
    boolean existsByEmail(String email
}