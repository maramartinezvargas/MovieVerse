package com.mara.tfgcine.service;

import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.TvSeries;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar los "likes" (me gusta) de los usuarios sobre películas y series.
 *
 * Permite alternar un like (crear o eliminar), comprobar si un usuario ya ha dado like
 * a un contenido, contar el número total de likes y obtener el historial de likes de un usuario.
 *
 * Para crear un like, guarda también un snapshot de datos de TMDB (título, póster y valoración)
 * para poder mostrar el contenido en el perfil aunque cambie en la API externa y para evitar
 * hacer excesivas llamadas a la API.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see Like
 * @see LikeRepository
 * @see TmdbService
 */
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final TmdbService tmdbService;

    public LikeService(
            LikeRepository likeRepository,
            TmdbService tmdbService
    ) {
        this.likeRepository = likeRepository;
        this.tmdbService = tmdbService;
    }

    /**
     * Alterna el toggle del like de un usuario sobre un contenido.
     *
     * Si el like ya existe, lo elimina. Si no existe, lo crea y guarda un snapshot
     * de los datos del contenido desde TMDB.
     *
     * @param user usuario que da o quita el like
     * @param mediaId identificador del contenido en TMDB
     * @param mediaType tipo de medio (película o serie)
     * @return {@code true} si el like queda activo, {@code false} si se elimina
     */

    public boolean toggleLike(User user, Long mediaId, MediaType mediaType) {

        Optional<Like> existing = likeRepository
                .findByUserAndMediaIdAndMediaType(user, mediaId, mediaType

        if (existing.isPresent()) {

            likeRepository.delete(existing.get()

            return false;

        } else {

            Like like = new Like(

            like.setUser(user
            like.setMediaId(mediaId
            like.setMediaType(mediaType
            like.setCreatedAt(LocalDateTime.now()

            try {

                // Snapshot de datos TMDB
                if (mediaType == MediaType.MOVIE) {

                    Movie movie = tmdbService.getMovieDetails(mediaId.intValue()

                    like.setTitle(movie.getTitle()
                    like.setPosterPath(movie.getPosterPath()
                    like.setVoteAverage(movie.getVoteAverage()

                } else {

                    TvSeries serie = tmdbService.getSerieDetails(mediaId.intValue()

                    like.setTitle(serie.getTitle()
                    like.setPosterPath(serie.getPosterPath()
                    like.setVoteAverage(serie.getVoteAverage()
                }

            } catch (Exception e) {

                e.printStackTrace(

                // fallback mínimo para evitar problemas en en like si TMDB falla
                like.setTitle("Contenido"
                like.setPosterPath(null
                like.setVoteAverage(0.0
            }

            likeRepository.save(like

            return true;
        }
    }

    /**
     * Comprueba si un usuario ya ha dado like a un contenido.
     *
     * @param user usuario a comprobar
     * @param mediaId identificador del contenido en TMDB
     * @param mediaType tipo de medio (película o serie)
     * @return {@code true} si ya ha dado like, {@code false} en caso contrario
     */
    public boolean hasUserLiked(User user, Long mediaId, MediaType mediaType) {
        return likeRepository.existsByUserAndMediaIdAndMediaType(
                user,
                mediaId,
                mediaType
        
    }


    /**
     * Cuenta el número total de likes de un contenido.
     *
     * @param mediaId identificador del contenido en TMDB
     * @param mediaType tipo de medio (película o serie)
     * @return número total de likes
     */
    public int countLikes(Long mediaId, MediaType mediaType) {
        return likeRepository.countByMediaIdAndMediaType(
                mediaId,
                mediaType
        
    }

    /**
     * Obtiene todos los likes de un usuario ordenados de más recientes a más antiguos.
     *
     * @param user usuario cuyos likes se quieren consultar
     * @return lista de likes del usuario
     */
    public List<Like> getUserLikes(User user) {
        return likeRepository.findByUserOrderByCreatedAtDesc(user
    }

}