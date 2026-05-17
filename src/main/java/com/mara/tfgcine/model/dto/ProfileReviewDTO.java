package com.mara.tfgcine.model.dto;

import com.mara.tfgcine.model.media.MediaType;

import java.time.LocalDateTime;

/**
 * DTO para representar las reseñas en el perfil del usuario.
 * Contiene información básica sobre la reseña, como el título del medio, el comentario, la calificación y la fecha de creación.
 */

public class ProfileReviewDTO {

    private Long id;
    private Long mediaId;
    private MediaType mediaType;

    private String title;
    private String posterPath;

    private String comment;
    private Integer rating;

    private LocalDateTime createdAt;

    public ProfileReviewDTO() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}