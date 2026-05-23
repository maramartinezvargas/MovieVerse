package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "reporter_id")
    private Long reporterId;

    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "reported_user_id")
    private Long reportedUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "moderator_id")
    private Long moderatorId;

    public Report() {
    }

    // CreatedAt y Status se establecen automáticamente al persistir con valores predeterminados
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(
        }
        if (status == null) {
            status = ReportStatus.PENDING;
        }
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Long getReportedUserId() {
        return reportedUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public void setReportedUserId(Long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModeratorId() {return moderatorId;}

    public void setModeratorId(Long moderatorId) {this.moderatorId = moderatorId;}
}