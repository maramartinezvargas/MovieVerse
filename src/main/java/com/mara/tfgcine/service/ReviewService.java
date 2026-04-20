package com.mara.tfgcine.service;

import com.mara.tfgcine.model.dto.ReviewDTO;
import com.mara.tfgcine.model.review.Review;
import com.mara.tfgcine.model.review.TmdbReview;
import com.mara.tfgcine.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TmdbService tmdbService;

    public ReviewService(ReviewRepository reviewRepository, TmdbService tmdbService) {
        this.reviewRepository = reviewRepository;
        this.tmdbService = tmdbService;
    }

    // Obtener todas las reviews (locales + TMDB) para una película o serie concreta, ordenadas por fecha (más recientes primero)
    public List<ReviewDTO> getAllReviews(Long mediaId, String mediaType) {

        List<ReviewDTO> local = reviewRepository
                .findByMediaIdAndMediaType(mediaId, mediaType)
                .stream()
                .map(this::mapLocalReview)
                .toList(

        List<ReviewDTO> tmdb;

        if ("tv".equals(mediaType)) {
            tmdb = tmdbService.getSerieReviews(mediaId
        } else {
            tmdb = tmdbService.getReviews(mediaId
        }

        return Stream.concat(local.stream(), tmdb.stream())
                .sorted(
                        Comparator.comparing(
                                ReviewDTO::getCreatedAt,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                )
                .toList(
    }

    // LOCAL
    private ReviewDTO mapLocalReview(Review review) {

        ReviewDTO dto = new ReviewDTO(

        if (review.getUser() != null) {
            dto.setUsername(review.getUser().getUsername()
        } else {
            dto.setUsername("Usuario MovieVerse"
        }

        dto.setComment(review.getComment()
        dto.setRating((double) review.getRating()
        dto.setCreatedAt(review.getCreatedAt()
        dto.setSource("LOCAL"

        return dto;
    }

    // TMDB
    private ReviewDTO mapTmdbReview(TmdbReview tmdb) {
        ReviewDTO dto = new ReviewDTO(

        dto.setUsername(tmdb.getAuthor()
        dto.setComment(tmdb.getContent()
        dto.setCreatedAt(
                OffsetDateTime.parse(tmdb.getCreatedAt()).toLocalDateTime()
        
        dto.setSource("TMDB"

        if (tmdb.getAuthorDetails() != null &&
                tmdb.getAuthorDetails().getRating() != null) {

            dto.setRating(tmdb.getAuthorDetails().getRating()

        } else {
            dto.setRating(null
        }

        return dto;
    }

    public void createReview(Long mediaId, String comment, Integer rating, String mediaType) {

        Review review = new Review(

        review.setMediaId(mediaId
        review.setMediaType(mediaType // CLAVE
        review.setComment(comment
        review.setRating(rating
        review.setCreatedAt(LocalDateTime.now()

        // usuario (deberia ir aqui el usuario real, pero de momento null)
        //User user = userService.getCurrentUser(
        review.setUser(null

        reviewRepository.save(review
    }
}