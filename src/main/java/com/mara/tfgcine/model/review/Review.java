package com.mara.tfgcine.model.review;

import com.mara.tfgcine.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
}