package com.mara.tfgcine.repository;
import com.mara.tfgcine.model.list.MediaList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaListRepository extends JpaRepository<MediaList, Long> {

    @Query("SELECT l FROM MediaList l LEFT JOIN FETCH l.items WHERE l.user.username = :username")
    List<MediaList> findWithItemsByUsername(@Param("username") String username
}