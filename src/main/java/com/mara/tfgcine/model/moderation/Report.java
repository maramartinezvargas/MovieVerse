package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue
    private Long id;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private Long reporterId;
    private Long reviewId;
    private Long reportedUserId;

    public void setStatus(ReportStatus reportStatus) {
        this.status = reportStatus;
    }
}