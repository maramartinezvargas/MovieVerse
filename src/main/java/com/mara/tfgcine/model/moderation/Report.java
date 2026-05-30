package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un reporte de una reseña realizado por un usuario.
 *
 * Cada instancia registra una denuncia/reporte sobre una reseña problemática
 * (contenido inapropiado, ofensivo, spam, etc.) y se persiste en la tabla {@code reports}
 * para que moderadores revisen y tomen acciones al respecto.
 *
 * Campos principales:
 * - reason: motivo o descripción del reporte
 * - status: estado actual del reporte (enum {@link ReportStatus}: PENDING, RESOLVED, REJECTED)
 * - reporterId: identificador del usuario que realiza el reporte
 * - reviewId: identificador de la reseña reportada
 * - reportedUserId: identificador del autor de la reseña reportada
 * - createdAt: fecha y hora en que se realizó el reporte (se establece automáticamente)
 * - moderatorId: identificador del moderador que resolvió el reporte (opcional)
 *
 * Ciclo de vida: el reporte se crea con estado PENDING, y un moderador puede
 * cambiar su estado a RESOLVED (si la reseña se elimina) o REJECTED (si se considera válida).
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see ReportStatus
 * @see com.mara.tfgcine.service.ModerationService
 */
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason", length = 200)
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