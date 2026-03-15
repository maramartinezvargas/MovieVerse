package com.mara.tfgcine.model.list;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class ListItem {

    @Id
    @GeneratedValue
    private Long id;

    private int mediaId;

    private LocalDateTime addedAt;

    @ManyToOne
    private MediaList list;

}