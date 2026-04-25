package com.mara.tfgcine.service;

import com.mara.tfgcine.model.user.AccountStatus;
import com.mara.tfgcine.model.user.Role;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Nombre de usuario \"" + user.getUsername() + "\" ya existe"
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("La cuenta de correo \"" + user.getEmail() + "\" ya existe"
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())
        user.setRole(Role.STANDARD
        user.setStatus(AccountStatus.ACTIVE

        userRepository.save(user
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