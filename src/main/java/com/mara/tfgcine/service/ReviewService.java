package com.mara.tfgcine.service;

import com.mara.tfgcine.model.dto.ReviewDTO;
import com.mara.tfgcine.model.review.Review;
import com.mara.tfgcine.model.review.TmdbReview;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.ReviewRepository;
import com.mara.tfgcine.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TmdbService tmdbService;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         TmdbService tmdbService,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.tmdbService = tmdbService;
        this.userRepository = userRepository;
    }

    // Obtener todas las reviews (locales + TMDB) para una película o serie concreta, ordenadas por fecha (más recientes primero)
    public List<ReviewDTO> getAllReviews(Long mediaId, String mediaType) {

        // Normalizo el mediaType para evitar problemas de mayúsculas/minúsculas
        mediaType = mediaType.toUpperCase(

        List<ReviewDTO> local = reviewRepository
                .findByMediaIdAndMediaType(mediaId, mediaType)
                .stream().map(this::mapLocalReview)
                .toList(

        List<ReviewDTO> tmdb;

        switch (mediaType) {
            case "SERIE" -> tmdb = tmdbService.getSerieReviews(mediaId
            case "MOVIE" -> tmdb = tmdbService.getReviews(mediaId
            default -> throw new IllegalArgumentException("Tipo inválido: " + mediaType
        }

        return Stream.concat(local.stream(), tmdb.stream())
                .sorted(Comparator.comparing(ReviewDTO::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed()
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

    public void createReview(String username,
                             Long mediaId,
                             String comment,
                             Integer rating,
                             String mediaType) {

        User user = userRepository.findByUsername(username

        mediaType = mediaType.toUpperCase(

        if (reviewRepository.existsByUserUsernameAndMediaIdAndMediaType(username, mediaId, mediaType)) {
            throw new IllegalStateException("duplicate"
        }

        Review review = new Review(
        review.setUser(user
        review.setMediaId(mediaId
        review.setMediaType(mediaType.toUpperCase() // Normalizado para evitar problemas de mayúsculas/minúsculas
        review.setComment(comment
        review.setRating(rating
        review.setCreatedAt(LocalDateTime.now()

        reviewRepository.save(review
    }
}