package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ModerationAction {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModerationActionType actionType;

    private Long moderatorId;
    private Long targetUserId;
    private Long targetReviewId;

    private LocalDateTime createdAt;

    private String reason;

}