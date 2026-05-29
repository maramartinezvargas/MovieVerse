package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
 * Repositorio Spring Data JPA para acceder a reseñas de películas y series.
 *
 * Proporciona operaciones CRUD sobre la entidad {@link com.mara.tfgcine.model.review.Review}
 * y consultas personalizadas para recuperar reseñas por medio, por usuario y para comprobar
 * si un usuario ya ha publicado una reseña sobre un contenido concreto.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.review.Review
 * @see com.mara.tfgcine.model.media.MediaType
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {


    /**
     * Obtiene las reseñas de un medio concreto ordenadas de más recientes a más antiguas.
     *
     * La consulta hace {@code JOIN FETCH} con el usuario para evitar consultas adicionales
     * al acceder a los datos del autor de la reseña.
     *
     * @param mediaId identificador del medio en TMDB
     * @param mediaType tipo de medio (película o serie)
     * @return lista de reseñas ordenadas por fecha de creación descendente
     */
    @Query("""
        SELECT r FROM Review r
        JOIN FETCH r.user
        WHERE r.mediaId = :mediaId
        AND r.mediaType = :mediaType
        ORDER BY r.createdAt DESC
        """)
    List<Review> findByMediaIdAndMediaType(@Param("mediaId") Long mediaId,
                                           @Param("mediaType") MediaType mediaType

    /**
     * Comprueba si un usuario ya ha escrito una reseña para un medio concreto.
     *
     * Se usa normalmente para impedir que un mismo usuario publique varias reseñas
     * sobre la misma película o serie.
     *
     * @param username nombre de usuario
     * @param mediaId identificador del medio en TMDB
     * @param mediaType tipo de medio (película o serie)
     * @return {@code true} si ya existe una reseña, {@code false} en caso contrario
     */
    boolean existsByUserUsernameAndMediaIdAndMediaType(String username, Long mediaId, MediaType mediaType

    /**
     * Obtiene todas las reseñas asociadas a un medio concreto.
     *
     * @param mediaId identificador del medio en TMDB
     * @return lista de reseñas de ese medio
     */
    List<Review> findByMediaId(@Param("mediaId") Long mediaId

    /**
     * Obtiene todas las reseñas escritas por un usuario concreto.
     *
     * @param userId identificador del usuario
     * @return lista de reseñas del usuario
     */
    List<Review> findByUserId(Long userId


}