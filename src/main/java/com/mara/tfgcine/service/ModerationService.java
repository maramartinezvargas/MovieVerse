package com.mara.tfgcine.service;

import com.mara.tfgcine.model.moderation.Report;
import com.mara.tfgcine.model.moderation.ReportStatus;
import com.mara.tfgcine.repository.AuditLogRepository;
import com.mara.tfgcine.repository.ReportRepository;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {

    private final ReportRepository reportRepository;
    private final AuditLogRepository auditLogRepository;

    public ModerationService(ReportRepository reportRepository,
                             AuditLogRepository auditLogRepository) {
        this.reportRepository = reportRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public void resolveReport(Long reportId) {

        Report report = reportRepository.findById(reportId).orElseThrow(

        report.setStatus(ReportStatus.RESOLVED

        reportRepository.save(report
    }

}