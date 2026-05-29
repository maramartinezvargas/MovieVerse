package com.mara.tfgcine.service;

import com.mara.tfgcine.model.dto.ReviewDTO;
import com.mara.tfgcine.model.media.MediaType;
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

/**
 * Servicio encargado de gestionar las reseñas de usuarios sobre películas y series.
 *
 * Permite crear reseñas locales, comprobar duplicados y recuperar todas las reseñas
 * asociadas a un contenido concreto, combinando reseñas internas de MovieVerse con
 * reseñas obtenidas desde TMDB.
 *
 * También realiza el mapeo entre entidades locales ({@link com.mara.tfgcine.model.review.Review})
 * y DTOs de presentación ({@link com.mara.tfgcine.model.dto.ReviewDTO}).
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see ReviewRepository
 * @see TmdbService
 * @see UserRepository
 * @see com.mara.tfgcine.model.review.Review
 * @see com.mara.tfgcine.model.dto.ReviewDTO
 */
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


    /**
    * Obtiene todas las reseñas de un contenido, tanto locales como de TMDB,
    * ordenadas de más recientes a más antiguas.
    *
    * @param mediaId identificador del contenido en TMDB
    * @param mediaType tipo de medio (película o serie)
    * @return lista de reseñas combinadas y ordenadas por fecha
    */
    public List<ReviewDTO> getAllReviews(Long mediaId, MediaType mediaType) {

        List<ReviewDTO> local = reviewRepository
                .findByMediaIdAndMediaType(mediaId, mediaType)
                .stream().map(this::mapLocalReview)
                .toList(

        List<ReviewDTO> tmdb;

        switch (mediaType) {
            case SERIE -> tmdb = tmdbService.getSerieReviews(mediaId
            case MOVIE -> tmdb = tmdbService.getReviews(mediaId
            default -> throw new IllegalArgumentException("Tipo inválido: " + mediaType
        }

        return Stream.concat(local.stream(), tmdb.stream())
                .sorted(Comparator.comparing(ReviewDTO::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed()
                )
                .toList(
    }

    // Convierte una review local a DTO para la capa de presentación
    private ReviewDTO mapLocalReview(Review review) {

        ReviewDTO dto = new ReviewDTO(

        if (review.getUser() != null) {
            dto.setUsername(review.getUser().getUsername()
        } else {
            dto.setUsername("Usuario MovieVerse"
        }

        dto.setId(review.getId() // Para poder eliminar la review localmente
        dto.setComment(review.getComment()
        dto.setRating((double) review.getRating()
        dto.setCreatedAt(review.getCreatedAt()
        dto.setSource("LOCAL"

        return dto;
    }

    /**
     * Mapea una reseña obtenida desde TMDB a un DTO (ReviewDTO)
     *
     * @Deprecated El mapeo de reseñas de TMDB se realiza directamente en el servicio de TMDB,
     * pero se mantiene este método privado por si se necesita en algún caso concreto.
     *
     * @param tmdb - reseña obtenida desde TMDB
     * @return dto - DTO con los datos de la reseña formateados para la presentación
     */
    // Convierte una review de TMDB a DTO para mostrarla junto con las locales
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

    /**
     * Crea una nueva reseña local para un usuario.
     *
     * Comprueba primero que el usuario no haya escrito ya una reseña sobre
     * el mismo contenido.
     *
     * @param username nombre de usuario autor de la reseña
     * @param mediaId identificador del contenido en TMDB
     * @param comment comentario de la reseña
     * @param rating puntuación otorgada
     * @param mediaType tipo de medio (película o serie)
     * @param title título del contenido
     * @param posterPath ruta del póster del contenido
     * @throws IllegalStateException si ya existe una reseña previa para ese contenido
     */
    public void createReview(String username,
                             Long mediaId,
                             String comment,
                             Integer rating,
                             MediaType mediaType,
                             String title,
                             String posterPath) {

        User user = userRepository.findByUsername(username

        if (reviewRepository.existsByUserUsernameAndMediaIdAndMediaType(username, mediaId, mediaType)) {
            throw new IllegalStateException("duplicate"
        }

        Review review = new Review(
        review.setUser(user
        review.setMediaId(mediaId
        review.setMediaType(MediaType.valueOf(mediaType.name())
        review.setComment(comment
        review.setRating(rating
        review.setCreatedAt(LocalDateTime.now()
        review.setTitle(title
        review.setPosterPath(posterPath

        reviewRepository.save(review
    }
}