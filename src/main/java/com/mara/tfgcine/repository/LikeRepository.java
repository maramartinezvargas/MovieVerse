package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.like.MediaType;
import com.mara.tfgcine.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // Buscar si ya existe un like
    Optional<Like> findByUserAndMediaIdAndMediaType(User user, Long mediaId, MediaType mediaType

    boolean existsByUserAndMediaIdAndMediaType(User user, Long mediaId, MediaType mediaType

    int countByMediaIdAndMediaType(Long mediaId, MediaType mediaType
}