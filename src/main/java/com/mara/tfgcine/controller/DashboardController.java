package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.ReportDashboardDTO;
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
            UserRepository userRepository)
    {
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

        model.addAttribute("reports", reports

        return "dashboard";
    }
}