package com.mara.tfgcine.service;

import com.mara.tfgcine.model.list.MediaList;
import com.mara.tfgcine.repository.MediaListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaListService {

    private final MediaListRepository mediaListRepository;

    public MediaListService(MediaListRepository mediaListRepository) {
        this.mediaListRepository = mediaListRepository;
    }

    public List<MediaList> getListsByUsername(String username) {
        return mediaListRepository.findWithItemsByUsername(username
    }
}