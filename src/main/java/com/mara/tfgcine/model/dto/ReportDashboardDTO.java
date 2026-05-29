package com.mara.tfgcine.model.dto;

import com.mara.tfgcine.model.moderation.ReportStatus;

import java.time.LocalDateTime;

/**
 * DTO utilizado para representar los reportes en el panel de administración (DASHBOARD)
 *
 * Contiene la información resumida necesaria para mostrar cada reporte en el dashboard,
 * incluyendo identificador, reseña asociada, nombres de usuario implicados, motivo,
 * estado y fecha de creación.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
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