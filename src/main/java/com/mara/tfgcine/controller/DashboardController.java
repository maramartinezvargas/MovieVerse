package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.ReportDashboardDTO;
import com.mara.tfgcine.model.moderation.ReportStatus;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.ReportRepository;
import com.mara.tfgcine.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public DashboardController(
            ReportRepository reportRepository,
            UserRepository userRepository
    ) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<ReportDashboardDTO> reports =
                reportRepository.findAll()
                        .stream()
                        .map(report -> {

                            User reporter = userRepository
                                    .findById(report.getReporterId())
                                    .orElse(null

                            User reported = userRepository
                                    .findById(report.getReportedUserId())
                                    .orElse(null

                            return new ReportDashboardDTO(
                                    report.getId(),

                                    report.getReviewId(),

                                    reporter != null
                                            ? reporter.getUsername()
                                            : "Desconocido",

                                    reported != null
                                            ? reported.getUsername()
                                            : "Desconocido",

                                    report.getReason(),

                                    report.getStatus(),

                                    report.getCreatedAt()
                            
                        })
                        .toList(

        long pendingCount = reports.stream()
                .filter(r -> r.getStatus() == ReportStatus.PENDING)
                .count(

        long underReviewCount = reports.stream()
                .filter(r -> r.getStatus() == ReportStatus.UNDER_REVIEW)
                .count(

        long resolvedCount = reports.stream()
                .filter(r -> r.getStatus() == ReportStatus.RESOLVED)
                .count(

        long rejectedCount = reports.stream()
                .filter(r -> r.getStatus() == ReportStatus.REJECTED)
                .count(

        model.addAttribute("reports", reports

        model.addAttribute("pendingCount", pendingCount

        model.addAttribute("underReviewCount", underReviewCount

        model.addAttribute("resolvedCount", resolvedCount

        model.addAttribute("rejectedCount", rejectedCount

        return "dashboard";
    }
}