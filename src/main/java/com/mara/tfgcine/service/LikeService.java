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
     * Toggle de like:
     * - Si existe → lo elimina
     * - Si no existe → lo crea
     *
     * @return true si queda like activo, false si se elimina
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
     * Saber si el usuario ya ha dado like
     */
    public boolean hasUserLiked(User user, Long mediaId, MediaType mediaType) {
        return likeRepository.existsByUserAndMediaIdAndMediaType(
                user,
                mediaId,
                mediaType
        
    }

    /**
     * Contar likes de un contenido
     */
    public int countLikes(Long mediaId, MediaType mediaType) {
        return likeRepository.countByMediaIdAndMediaType(
                mediaId,
                mediaType
        
    }

    /**
     * Obtener todos los likes de un usuario
     */
    public List<Like> getUserLikes(User user) {
        return likeRepository.findByUserOrderByCreatedAtDesc(user
    }

}