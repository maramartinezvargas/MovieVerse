package com.mara.tfgcine.controller;

import com.mara.tfgcine.repository.ReportRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ReportRepository reportRepository;

    public DashboardController(
            ReportRepository reportRepository
    ) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "reports",
                reportRepository.findAll()
        

        return "dashboard";
    }
}