package com.mara.tfgcine.service;

import com.mara.tfgcine.model.status.MediaStatus;
import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.status.UserMediaStatus;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.UserMediaStatusRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserMediaStatusService {

    private final UserMediaStatusRepository
            userMediaStatusRepository;

    public UserMediaStatusService(
            UserMediaStatusRepository userMediaStatusRepository
    ) {

        this.userMediaStatusRepository =
                userMediaStatusRepository;

    }

    /* Obtener estado actual */

    public Optional<UserMediaStatus> getStatus(
            User user,
            Long mediaId,
            MediaType mediaType
    ) {

        return userMediaStatusRepository
                .findByUserAndMediaIdAndMediaType(
                        user,
                        mediaId,
                        mediaType
                

    }

    /* Guardar o actualizar estado */
    public UserMediaStatus saveStatus(
            User user,
            Long mediaId,
            MediaType mediaType,
            String title,
            String posterPath,
            Double voteAverage,
            MediaStatus status
    ) {

        Optional<UserMediaStatus> existingStatus =
                getStatus(user, mediaId, mediaType

        if (existingStatus.isPresent()) {

            UserMediaStatus currentStatus =
                    existingStatus.get(

            /* Toggle: si pulsa el mismo estado, eliminar */

            if (currentStatus.getStatus() == status) {

                userMediaStatusRepository
                        .delete(currentStatus

                return null;
            }

            /* Cambiar estado */

            currentStatus.setStatus(status

            return userMediaStatusRepository
                    .save(currentStatus

        }

        /* Crear nuevo estado */

        UserMediaStatus newStatus =
                new UserMediaStatus(

        newStatus.setUser(user
        newStatus.setMediaId(mediaId
        newStatus.setMediaType(mediaType
        newStatus.setStatus(status
        newStatus.setTitle(title
        newStatus.setPosterPath(posterPath
        newStatus.setVoteAverage(voteAverage

        return userMediaStatusRepository
                .save(newStatus

    }

    /// Obtener todos los títulos con un estado específico para un usuario
    public List<UserMediaStatus> getStatusesByType(User user, MediaStatus status) {
        return userMediaStatusRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status
    }

}