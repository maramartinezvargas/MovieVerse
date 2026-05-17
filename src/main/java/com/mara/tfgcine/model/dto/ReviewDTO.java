package com.mara.tfgcine.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReviewDTO {

    private Long id;
    private String username;
    private String comment;
    private Double rating;
    private LocalDateTime createdAt;
    private String source; // "LOCAL" o "TMDB"
    private String avatarUrl; // Imagen Avatar TMDB user

    // getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewDTO)) return false;
        ReviewDTO that = (ReviewDTO) o;
        return Objects.equals(comment, that.comment
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment
    }
}