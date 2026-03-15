package com.mara.tfgcine.model.review;

import com.mara.tfgcine.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    private int rating;
    private String comment;

    private LocalDateTime createdAt;

    private int mediaId;

    @ManyToOne
    private User user;

    public void setRating(int rating) {
    }

    public void setComment(String comment) {
    }

    public void setCreatedAt(LocalDateTime now) {
    }

    public void setMediaId(int mediaId) {
    }
}