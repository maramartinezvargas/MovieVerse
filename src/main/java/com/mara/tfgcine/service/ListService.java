package com.mara.tfgcine.service;

import com.mara.tfgcine.model.list.MediaList;
import com.mara.tfgcine.repository.ListItemRepository;
import com.mara.tfgcine.repository.MediaListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListService {

    private final MediaListRepository listRepository;
    private final ListItemRepository itemRepository;

    public ListService(MediaListRepository listRepository,
                       ListItemRepository itemRepository) {
        this.listRepository = listRepository;
        this.itemRepository = itemRepository;
    }

    public List<MediaList> getUserLists(Long userId) {
        return listRepository.findByUserId(userId
    }

}