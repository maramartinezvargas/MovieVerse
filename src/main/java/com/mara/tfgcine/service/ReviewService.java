package com.mara.tfgcine.service;

import com.mara.tfgcine.model.review.Review;
import com.mara.tfgcine.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(Long userId, int mediaId, int rating, String comment) {

        Review review = new Review(
        review.setRating(rating
        review.setComment(comment
        review.setCreatedAt(LocalDateTime.now()
        review.setMediaId(mediaId

        return reviewRepository.save(review
    }

}