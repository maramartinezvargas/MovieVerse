package com.mara.tfgcine.service;

import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.like.MediaType;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
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

            likeRepository.save(like
            return true;
        }
    }

    /**
     * Saber si el usuario ya ha dado like
     */
    public boolean hasUserLiked(User user, Long mediaId, MediaType mediaType) {
        return likeRepository.existsByUserAndMediaIdAndMediaType(user, mediaId, mediaType
    }

    /**
     * Contar likes de un contenido
     */
    public int countLikes(Long mediaId, MediaType mediaType) {
        return likeRepository.countByMediaIdAndMediaType(mediaId, mediaType
    }
}