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

    // Obtener todas las reviews (locales + TMDB) para una película o serie
    public List<ReviewDTO> getAllReviews(Long mediaId) {

        List<ReviewDTO> local = reviewRepository.findByMediaId(mediaId)
                .stream()
                .map(this::mapLocalReview)
                .toList(

        List<ReviewDTO> tmdb = tmdbService.getReviews(mediaId

        // Combinar, ordenar por fecha (más reciente primero) y devolver
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

        dto.setUsername(review.getUser().getUsername()
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

    public void createReview(Long mediaId, String comment, Integer rating) {

        Review review = new Review(

        review.setMediaId(mediaId
        review.setComment(comment
        review.setRating(rating
        review.setCreatedAt(LocalDateTime.now()

        // Aaquí deberías poner el usuario real (login)
        review.setUser(null

        reviewRepository.save(review
    }
}