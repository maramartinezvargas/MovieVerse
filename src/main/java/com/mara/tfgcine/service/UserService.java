package com.mara.tfgcine.service;

import com.mara.tfgcine.model.user.AccountStatus;
import com.mara.tfgcine.model.user.Role;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de gestión de usuarios.
 *
 * Proporciona operaciones de autenticación y búsqueda de usuarios: registro de nuevas cuentas,
 * búsqueda por nombre de usuario o email, validación de duplicados y cifrado de contraseñas.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see User
 * @see com.mara.tfgcine.repository.UserRepository
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en la aplicación.
     *
     * Valida que el nombre de usuario y email sean únicos, cifra la contraseña,
     * asigna rol STANDARD y estado ACTIVE por defecto, y persiste el usuario.
     *
     * @param user objeto User con username, email y password
     * @throws RuntimeException si el nombre de usuario o email ya existen
     */
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

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre único del usuario
     * @return objeto User si existe
     * @throws RuntimeException si el usuario no existe
     */
    public User findByUsername(String username) {

        User user = userRepository.findByUsername(username

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado"
        }

        return user;
    }

    /**
     * Busca un usuario por su dirección de correo.
     *
     * @param email dirección de correo del usuario
     * @return objeto User si existe
     * @throws RuntimeException si el usuario no existe
     */
    public User findByEmail(String email) {

        User user = userRepository.findByEmail(email

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado"
        }

        return user;
    }



}