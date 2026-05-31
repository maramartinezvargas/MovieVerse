package com.mara.tfgcine.model.status;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.user.User;

import com.mara.tfgcine.service.UserMediaStatusService;
import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad JPA que registra el estado de visualización de un usuario sobre una película o serie.
 *
 * Cada instancia representa que un usuario ha marcado un contenido multimedia como visto (WATCHED)
 * o añadido a su lista de pendientes (WATCHLIST) y se persiste en la tabla {@code user_media_status}.
 *
 * Campos principales:
 * - user: relación con el usuario propietario del estado
 * - mediaId: identificador de la película o serie en TMDB
 * - mediaType: tipo de medio (enum {@link MediaStatus}: WATCHED o WATCHLIST)
 * - status: estado actual del seguimiento
 * - createdAt: fecha de creación (se establece automáticamente)
 * - title: snapshot del título del medio para visualización sin consultar TMDB
 * - posterPath: snapshot de la ruta del póster para visualización sin consultar TMDB
 * - voteAverage: snapshot de la calificación promedio del medio
 *
 * Restricciones: una combinación única (usuario, película/serie, tipo de medio) garantiza
 * que un usuario no puede tener múltiples estados para el mismo contenido.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see MediaStatus
 * @see com.mara.tfgcine.model.user.User
 * @see com.mara.tfgcine.model.media.MediaType
 */

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "user_id",
            "media_id",
            "media_type"
        })
    }
)

public class UserMediaStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private Long mediaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    /* SNAPSHOT TMDB */
    @Column(nullable = false)
    private String title;
    private String posterPath;
    private Double voteAverage;

    /* STATUS */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* Getters and Setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}