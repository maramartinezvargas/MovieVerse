package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.moderation.Report;
import com.mara.tfgcine.model.moderation.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para acceder a reportes de moderación.
 *
 * Proporciona operaciones CRUD sobre la entidad {@link com.mara.tfgcine.model.moderation.Report}
 * y consultas personalizadas para el panel de moderación.
 *
 * Métodos personalizados:
 * - findByReviewId(Long): obtiene todos los reportes asociados a una reseña concreta
 * - findByStatusIn(List<ReportStatus>): obtiene reportes cuyo estado está dentro de una lista de estados
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.moderation.Report
 * @see com.mara.tfgcine.model.moderation.ReportStatus
 */

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // Buscar todos reportes por id de review (para dashboard)
    List<Report> findByReviewId(Long reviewId

    // Buscar todos reportes por status de reporte (para dashboard)
    List<Report> findByStatusIn(List<ReportStatus> statuses
}