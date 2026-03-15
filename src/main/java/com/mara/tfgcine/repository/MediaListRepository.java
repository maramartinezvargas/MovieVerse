package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.list.MediaList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaListRepository extends JpaRepository<MediaList, Long> {

    List<MediaList> findByUserId(Long userId
}