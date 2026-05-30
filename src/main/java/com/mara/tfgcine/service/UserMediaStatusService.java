package com.mara.tfgcine.service;

import com.mara.tfgcine.model.status.MediaStatus;
import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.status.UserMediaStatus;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.UserMediaStatusRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar el estado de visualización de películas y series para cada usuario.
 *
 * Permite registrar si un usuario ha visto un contenido (WATCHED) o lo tiene en su lista
 * de pendientes (WATCHLIST). Implementa lógica de toggle: si el usuario pulsa el mismo estado,
 * se elimina el registro.
 *
 * Métodos principales:
 * - getStatus(): obtiene el estado actual de un medio para un usuario específico
 * - saveStatus(): guarda o actualiza el estado, incluyendo snapshot data del medio
 *   (si el usuario pulsa el mismo estado dos veces, se elimina el registro)
 * - getStatusesByType(): obtiene todos los medios de un usuario con un estado específico
 *
 * Campos snapshot (TMDB): Se almacenan title, posterPath y voteAverage para visualización
 * sin necesidad de hacer consultas adicionales a TMDB.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see UserMediaStatus
 * @see MediaStatus
 * @see com.mara.tfgcine.repository.UserMediaStatusRepository
 */
@Service
public class UserMediaStatusService {

    private final UserMediaStatusRepository
            userMediaStatusRepository;

    public UserMediaStatusService(
            UserMediaStatusRepository userMediaStatusRepository
    ) {

        this.userMediaStatusRepository = userMediaStatusRepository;
    }

    /**
     * Obtiene el estado actual de visualización de un medio para un usuario.
     *
     * @param user usuario propietario del estado
     * @param mediaId identificador de la película o serie en TMDB
     * @param mediaType tipo de medio (MOVIE o SERIE)
     * @return Optional con el UserMediaStatus si existe, vacío si no
     */
    public Optional<UserMediaStatus> getStatus(User user, Long mediaId, MediaType mediaType) {
        return userMediaStatusRepository.findByUserAndMediaIdAndMediaType(user, mediaId, mediaType
    }

    /**
     * Guarda o actualiza el estado de visualización de un medio para un usuario.
     *
     * Implementa lógica de toggle: si el usuario ya tiene ese estado en ese medio
     * y vuelve a pulsarlo, se elimina el registro.
     *
     * Captura snapshot data del medio (título, póster, calificación) para
     * visualización sin consultas adicionales a TMDB.
     *
     * @param user usuario propietario del estado
     * @param mediaId identificador de la película o serie en TMDB
     * @param mediaType tipo de medio (MOVIE o SERIE)
     * @param title título del medio (snapshot TMDB)
     * @param posterPath ruta del póster (snapshot TMDB)
     * @param voteAverage calificación promedio (snapshot TMDB)
     * @param status nuevo estado (WATCHED o WATCHLIST)
     * @return UserMediaStatus guardado, o null si se eliminó por toggle
     */
    public UserMediaStatus saveStatus(
            User user,
            Long mediaId,
            MediaType mediaType,
            String title,
            String posterPath,
            Double voteAverage,
            MediaStatus status
    ) {

        Optional<UserMediaStatus> existingStatus = getStatus(user, mediaId, mediaType

        if (existingStatus.isPresent()) {

            UserMediaStatus currentStatus = existingStatus.get(

            /* Toggle: eliminar estado (si pulsa el mismo que el actual) */
            if (currentStatus.getStatus() == status) {
                userMediaStatusRepository.delete(currentStatus
                return null;
            }

            /* Toggle: guardar nuevo estado (diferente al actual) */
            currentStatus.setStatus(status
            return userMediaStatusRepository.save(currentStatus
        }

        /* Crear nuevo estado */
        UserMediaStatus newStatus = new UserMediaStatus(

        newStatus.setUser(user
        newStatus.setMediaId(mediaId
        newStatus.setMediaType(mediaType
        newStatus.setStatus(status
        newStatus.setTitle(title
        newStatus.setPosterPath(posterPath
        newStatus.setVoteAverage(voteAverage

        return userMediaStatusRepository.save(newStatus

    }

    /**
     * Obtiene todos los medios de un usuario con un estado específico.
     *
     * @param user usuario propietario de los estados
     * @param status estado a filtrar (WATCHED o WATCHLIST)
     * @return lista de UserMediaStatus ordenados por fecha de creación descendente
     */
    public List<UserMediaStatus> getStatusesByType(User user, MediaStatus status) {
        return userMediaStatusRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status
    }

}