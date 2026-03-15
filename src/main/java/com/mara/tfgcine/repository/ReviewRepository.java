package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMediaId(int mediaId

    List<Review> findByUserId(Long userId

}