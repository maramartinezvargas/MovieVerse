package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para acceder a registros de likes de usuarios.
 *
 * Proporciona operaciones CRUD y consultas personalizadas sobre la entidad {@link com.mara.tfgcine.model.like.Like}.
 * Se utiliza para gestionar los contenidos (películas/series) que los usuarios marcan como favoritos.
 *
 * Métodos personalizados:
 * - findByUserAndMediaIdAndMediaType(): busca si existe un like específico de un usuario
 * - existsByUserAndMediaIdAndMediaType(): verifica si un usuario ya ha dado like a un medio
 * - countByMediaIdAndMediaType(): cuenta el total de likes de un medio específico
 * - findByUserOrderByCreatedAtDesc(): obtiene todos los likes de un usuario (ordenados por fecha descendente)
 * - findByUser(): obtiene todos los likes de un usuario
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.like.Like
 */
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
