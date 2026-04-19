package com.mara.tfgcine.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbReview {

    private String author;
    private String content;

    @JsonProperty("created_at")
    private String createdAt; // string para evitar problemas de parsing

    @JsonProperty("author_details")
    private AuthorDetails authorDetails;

    public static class AuthorDetails {
        private Double rating;

        @JsonProperty("avatar_path")
        private String avatarPath;

        public Double getRating() { return rating; }
        public void setRating(Double rating) { this.rating = rating; }

        public String getAvatarPath() { return avatarPath; }
        public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public AuthorDetails getAuthorDetails() { return authorDetails; }
    public void setAuthorDetails(AuthorDetails authorDetails) { this.authorDetails = authorDetails; }
}