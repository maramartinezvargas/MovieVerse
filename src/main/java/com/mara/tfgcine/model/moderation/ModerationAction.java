package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "moderation_actions")
public class ModerationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ModerationActionType actionType;

    @Column(name = "moderator_id")
    private Long moderatorId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "target_review_id")
    private Long targetReviewId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reason")
    private String reason;

    public ModerationAction() {}
}