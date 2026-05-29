package com.mara.tfgcine.model.user;

import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.review.Review;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Entidad JPA que representa un usuario de la aplicación.
 *
 * Cada instancia almacena la información de autenticación y perfil de un usuario,
 * así como sus roles y estado de cuenta. Se persiste en la tabla {@code users}.
 *
 * Campos principales:
 * - username: nombre único de usuario para acceso a la aplicación
 * - email: correo electrónico único del usuario
 * - password: contraseña encriptada
 * - role: rol asignado (enum {@link Role}: ADMIN, MODERATOR, STANDARD)
 * - status: estado de la cuenta (enum {@link AccountStatus}: ACTIVE, BANNED, DISABLED)
 * - reviews: lista de reseñas creadas por el usuario (relación uno-a-muchos)
 * - likes: lista de contenidos marcados como favoritos (relación uno-a-muchos)
 *
 * Relaciones:
 * - Un usuario puede crear múltiples reseñas ({@link com.mara.tfgcine.model.review.Review})
 * - Un usuario puede dar like a múltiples películas/series ({@link com.mara.tfgcine.model.like.Like})
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see Role
 * @see AccountStatus
 * @see com.mara.tfgcine.model.review.Review
 * @see com.mara.tfgcine.model.like.Like
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    // Relación con reviews
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("createdAt DESC") // Ordenar por fecha de creación descendente
    private List<Review> reviews;

    // Relación de likes (me gusta)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes;

    public User() {}

    // GETTERS & SETTERS
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Like> getLikes() {return likes;}

    public void setLikes(List<Like> likes) {this.likes = likes;}*/
}