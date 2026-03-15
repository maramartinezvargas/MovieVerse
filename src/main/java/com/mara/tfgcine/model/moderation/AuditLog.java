package com.mara.tfgcine.model.moderation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue
    private Long id;

    private Long moderatorId;

    private String action;

    private Long targetId;

    private LocalDateTime timestamp;

    private String details;

}