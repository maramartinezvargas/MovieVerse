package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.status.MediaStatus;
import com.mara.tfgcine.model.status.UserMediaStatus;
import com.mara.tfgcine.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMediaStatusRepository extends JpaRepository<UserMediaStatus, Long> {

    /* Query para encontrar el estado de un título específico para un usuario */
    Optional<UserMediaStatus> findByUserAndMediaIdAndMediaType(User user, Long mediaId, MediaType mediaType

    List<UserMediaStatus> findByUserAndStatusOrderByCreatedAtDesc(User user, MediaStatus status

}