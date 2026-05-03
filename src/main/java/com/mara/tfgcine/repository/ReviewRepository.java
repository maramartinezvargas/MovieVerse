package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT r FROM Review r
        JOIN FETCH r.user
        WHERE r.mediaId = :mediaId
        AND r.mediaType = :mediaType
        ORDER BY r.createdAt DESC
        """)
    List<Review> findByMediaIdAndMediaType(@Param("mediaId") Long mediaId,
                                           @Param("mediaType") String mediaType

    List<Review> findByMediaId(@Param("mediaId") Long mediaId

    List<Review> findByUserId(Long userId

    boolean existsByUserUsernameAndMediaIdAndMediaType(String username, Long mediaId, String mediaType
}