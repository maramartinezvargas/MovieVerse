package com.mara.tfgcine.service;

import com.mara.tfgcine.model.moderation.Report;
import com.mara.tfgcine.model.moderation.ReportStatus;
import com.mara.tfgcine.repository.AuditLogRepository;
import com.mara.tfgcine.repository.ReportRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la moderación de reportes.
 *
 * Actualmente permite resolver un reporte cambiando su estado a {@link ReportStatus#RESOLVED}
 * y persistiendo el cambio en la base de datos.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see Report
 * @see ReportStatus
 * @see ReportRepository
 * @see AuditLogRepository
 */
@Service
public class ModerationService {

    private final ReportRepository reportRepository;
    private final AuditLogRepository auditLogRepository;

    public ModerationService(ReportRepository reportRepository,
                             AuditLogRepository auditLogRepository) {
        this.reportRepository = reportRepository;
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Marca un reporte como resuelto.
     *
     * Busca el reporte por su identificador, cambia su estado a {@code RESOLVED}
     * y guarda la modificación.
     *
     * @param reportId identificador del reporte a resolver
     * @throws java.util.NoSuchElementException si el reporte no existe
     */
    public void resolveReport(Long reportId) {

        Report report = reportRepository.findById(reportId).orElseThrow(

        report.setStatus(ReportStatus.RESOLVED

        reportRepository.save(report
    }

}