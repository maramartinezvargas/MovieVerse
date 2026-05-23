package com.mara.tfgcine.model.dto;

import java.time.LocalDateTime;

public class ReviewResponseDTO {

    private Long id;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    private Long mediaId;

    private String mediaType;

    private String username;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(Long id,
                             Integer rating,
                             String comment,
                             LocalDateTime createdAt,
                             Long mediaId,
                             String mediaType,
                             String username) {

        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public Integer getRating() {
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

    public String getMediaType() {
        return mediaType;
    }

    public String getUsername() {
        return username;
    }
}