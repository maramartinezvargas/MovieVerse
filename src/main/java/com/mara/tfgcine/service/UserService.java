package com.mara.tfgcine.service;

import com.mara.tfgcine.model.user.AccountStatus;
import com.mara.tfgcine.model.user.Role;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        user.setRole(Role.STANDARD
        user.setStatus(AccountStatus.ACTIVE
        return userRepository.save(user
    }

    public User findByUsername(String username) {

        User user = userRepository.findByUsername(username

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado"
        }

        return user;
    }

    public User findByEmail(String email) {

        User user = userRepository.findByEmail(email

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado"
        }

        return user;
    }

}