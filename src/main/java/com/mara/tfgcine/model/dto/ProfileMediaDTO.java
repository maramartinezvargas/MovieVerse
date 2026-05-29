package com.mara.tfgcine.model.dto;

import java.time.LocalDateTime;


/**
 * DTO utilizado para representar contenido multimedia en el perfil de usuario.
 *
 * Contiene la información básica necesaria para mostrar títulos guardados,
 * marcados como vistos o añadidos a la lista de seguimiento, incluyendo identificador,
 * tipo de contenido, título, póster, fecha de creación y valoración media.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
public class ProfileMediaDTO {

    private Long mediaId;
    private String mediaType;

    private String title;
    private String posterPath;
    private LocalDateTime createdAt;
    private Double voteAverage;

    // Getters & Setters
    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }
}