package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.like.MediaType;
import com.mara.tfgcine.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // Buscar si ya existe un like
    Optional<Like> findByUserAndMediaIdAndMediaType(User user, Long mediaId, MediaType mediaType

    // Verificar si un usuario ya ha dado like a un medio específico
    boolean existsByUserAndMediaIdAndMediaType(User user, Long mediaId, MediaType mediaType

    // Contar el número de likes para un titulo específico
    int countByMediaIdAndMediaType(Long mediaId, MediaType mediaType

    // Obtener todos los likes de un usuario ordenados por fecha de creación (más recientes primero)
    List<Like> findByUserOrderByCreatedAtDesc(User user

    // Obtener todos los likes de un usuario
    List<Like> findByUser(User user
}
