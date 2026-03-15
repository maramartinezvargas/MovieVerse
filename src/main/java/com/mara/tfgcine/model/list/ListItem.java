package com.mara.tfgcine.model.list;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "list_items")
public class ListItem {

    @Id
    @GeneratedValue
    private Long id;

    private int mediaId;

    private LocalDateTime addedAt;

    @ManyToOne
    private MediaList list;

}