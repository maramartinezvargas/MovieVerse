package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositorio Spring Data JPA para acceder a usuarios de la aplicación.
 *
 * Proporciona operaciones CRUD sobre la entidad {@link com.mara.tfgcine.model.user.User}
 * y consultas personalizadas para buscar usuarios por nombre, email o comprobar
 * si ya existen credenciales duplicadas.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.user.User
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // obtiene un usuario por su nombre de usuario
    User findByUsername(String username

    // obtiene un usuario por su email
    User findByEmail(String email

    // comprueba si ya existe un nombre de usuario
    boolean existsByUsername(String username

    // comprueba si ya existe un email
    boolean existsByEmail(String email

    // obtiene un usuario por su identificador
    User findById(long id
}