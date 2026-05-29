package com.mara.tfgcine.model.like;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * Entidad que representa un "like" (me gusta) de un usuario sobre un contenido multimedia.
 *
 * Almacena la relación entre el usuario y el medio marcado como favorito,
 * junto con información auxiliar como el título, el póster, la valoración media
 * y la fecha en la que se creó el like.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_id", nullable = false)
    private Long mediaId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * SNAPSHOT: Datos auxiliares de TMDB utilizados para mostrar el contenido en el perfil del usuario.
     *
     * Se almacenan el título, el póster y la valoración media del medio para evitar
     * volver a consultar TMDB cada vez que se renderiza la lista de likes.
     */
    private String title;
    private String posterPath;
    private Double voteAverage;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getPosterPath() {return posterPath;}

    public void setPosterPath(String posterPath) {this.posterPath = posterPath;}

    public Double getVoteAverage() {return voteAverage;}

    public void setVoteAverage(Double voteAverage) {this.voteAverage = voteAverage;}
}