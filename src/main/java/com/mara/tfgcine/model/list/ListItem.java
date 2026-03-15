package com.mara.tfgcine.model.list;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "list_items")
public class ListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_id")
    private int mediaId;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private MediaList list;

    public ListItem() {}
}