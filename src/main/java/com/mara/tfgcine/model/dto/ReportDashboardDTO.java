package com.mara.tfgcine.model.dto;

import com.mara.tfgcine.model.moderation.ReportStatus;

import java.time.LocalDateTime;

public class ReportDashboardDTO {

    private Long id;

    private Long reviewId;

    private String reporterUsername;

    private String reportedUsername;

    private String reason;

    private ReportStatus status;

    private LocalDateTime createdAt;

    public ReportDashboardDTO(
            Long id,
            Long reviewId,
            String reporterUsername,
            String reportedUsername,
            String reason,
            ReportStatus status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.reviewId = reviewId;
        this.reporterUsername = reporterUsername;
        this.reportedUsername = reportedUsername;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public String getReporterUsername() {
        return reporterUsername;
    }

    public String getReportedUsername() {
        return reportedUsername;
    }

    public String getReason() {
        return reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}