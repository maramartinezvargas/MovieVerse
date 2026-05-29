package com.mara.tfgcine.model.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.model.media.MediaType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una reseña de usuario sobre una película o serie.
 *
 * Cada instancia almacena la opinión, calificación y comentario que un usuario
 * realiza sobre un contenido multimedia y se persiste en la tabla {@code reviews}.
 *
 * Campos principales:
 * - rating: calificación numérica de 1 a 10 que el usuario asigna
 * - comment: comentario o texto de la reseña
 * - mediaId: identificador de la película o serie reseñada (de TMDB)
 * - mediaType: tipo de medio (enum {@link com.mara.tfgcine.model.media.MediaType}: MOVIE o SERIE)
 * - user: relación con el usuario autor de la reseña
 * - createdAt: fecha y hora en que se creó la reseña
 * - title: snapshot del título del medio para mostrar en el perfil del usuario
 * - posterPath: snapshot de la ruta del póster para mostrar en el perfil del usuario
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.user.User
 * @see com.mara.tfgcine.model.media.MediaType
 */


@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "media_id", nullable = false)
    private Long mediaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Snapshot de título y poster para mostrar en perfil
    private String title;
    private String posterPath;

    public Review() {}

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public User getUser() {
        return user;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }



}