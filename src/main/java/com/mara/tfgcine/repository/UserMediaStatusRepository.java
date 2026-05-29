package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.status.MediaStatus;
import com.mara.tfgcine.model.status.UserMediaStatus;
import com.mara.tfgcine.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para acceder a los estados de visualización de un usuario.
 *
 * Proporciona operaciones CRUD sobre la entidad {@link com.mara.tfgcine.model.status.UserMediaStatus}
 * y consultas personalizadas para localizar el estado de un contenido concreto o recuperar
 * los títulos de un usuario filtrados por estado (WATCHED / WATCHLIST).
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.status.UserMediaStatus
 * @see com.mara.tfgcine.model.status.MediaStatus
 */
public interface UserMediaStatusRepository extends JpaRepository<UserMediaStatus, Long> {

    /**
     * Busca el estado de un contenido concreto para un usuario.
     *
     * Se usa para comprobar si ya existe un registro de estado antes de crear uno nuevo
     * o para actualizar/eliminar el actual.
     *
     * @param user usuario propietario del estado
     * @param mediaId identificador de la película o serie en TMDB
     * @param mediaType tipo de medio (película o serie)
     * @return Optional con el estado si existe, vacío si no (puede haber un UserMediaStatus o puede no haber nada)
     */
    Optional<UserMediaStatus> findByUserAndMediaIdAndMediaType(User user, Long mediaId, MediaType mediaType


    /**
     * Obtiene todos los contenidos de un usuario con un estado concreto,
     * ordenados por fecha de creación descendente.
     *
     * @param user usuario propietario de los estados
     * @param status estado a filtrar (WATCHED o WATCHLIST)
     * @return lista de estados ordenada de más recientes a más antiguos
     */
    List<UserMediaStatus> findByUserAndStatusOrderByCreatedAtDesc(User user, MediaStatus status

}