package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.list.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListItemRepository extends JpaRepository<ListItem, Long> {

    List<ListItem> findByListId(Long listId

}