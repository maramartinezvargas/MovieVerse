package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.moderation.Report;
import com.mara.tfgcine.model.moderation.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // Buscar todos reportes por id de review (para dashboard)
    List<Report> findByReviewId(Long reviewId

    // Buscar todos reportes por status de reporte (para dashboard)
    List<Report> findByStatusIn(List<ReportStatus> statuses
}