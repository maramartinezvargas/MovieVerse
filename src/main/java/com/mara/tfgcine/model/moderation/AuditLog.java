package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "moderator_id")
    private Long moderatorId;

    @Column(name = "action")
    private String action;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "details")
    private String details;
}