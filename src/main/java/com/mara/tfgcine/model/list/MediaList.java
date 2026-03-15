package com.mara.tfgcine.model.list;

import com.mara.tfgcine.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_lists")
public class MediaList {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String type;

    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

}