package com.mara.tfgcine.model.list;

import com.mara.tfgcine.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
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