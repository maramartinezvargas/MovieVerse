package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.ReportDashboardDTO;
import com.mara.tfgcine.model.moderation.ReportStatus;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.ReportRepository;
import com.mara.tfgcine.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;


/**
 * Controlador del panel de administración.
 *
 * Recupera los reportes existentes y prepara los datos necesarios para
 * mostrar el dashboard con estadísticas de estado y listado de incidencias.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
@Controller
public class DashboardController {

    // Instancias de los repositorios para acceder a los datos de reportes y usuarios
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    // Constructor
    public DashboardController(
            ReportRepository reportRepository,
            UserRepository userRepository
    ) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }


    /**
     * Muestra el panel de administración con los reportes y sus estadísticas.
     *
     * Consulta todos los reportes, resuelve los nombres de usuario del usuario
     * denunciante y del usuario denunciado, y calcula el número de reportes por estado
     * para mostrarlos en la vista del dashboard.
     *
     * @param model modelo de Spring MVC para enviar los datos a la vista
     * @return nombre de la vista del dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Convertir los reportes a DTO para mostrarlos en el dashboard
        List<ReportDashboardDTO> reports = reportRepository.findAll().stream()
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
                            reporter != null ? reporter.getUsername() : "Desconocido",
                            reported != null ? reported.getUsername() : "Desconocido",
                            report.getReason(),
                            report.getStatus(),
                            report.getCreatedAt()
                    
                })
                .toList(

        reports = reports.stream()
                .sorted(
                        Comparator.comparing((ReportDashboardDTO r) -> r.getStatus() != ReportStatus.PENDING)
                                .thenComparing(ReportDashboardDTO::getCreatedAt, Comparator.reverseOrder())
                )
                .toList(

        // Calcular el número de reportes por estado que se muestran en las stats
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

        // Enviar datos y estadísticas a la vista
        model.addAttribute("reports", reports

        model.addAttribute("pendingCount", pendingCount

        model.addAttribute("underReviewCount", underReviewCount

        model.addAttribute("resolvedCount", resolvedCount

        model.addAttribute("rejectedCount", rejectedCount

        return "dashboard";
    }

    @GetMapping("/test500")
    public String test500() {

        throw new RuntimeException("Test 500"

    }
}