package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.moderation.Report;
import com.mara.tfgcine.model.moderation.ReportStatus;
import com.mara.tfgcine.model.review.Review;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.ReportRepository;
import com.mara.tfgcine.repository.ReviewRepository;
import com.mara.tfgcine.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReportController(ReportRepository reportRepository,
                            ReviewRepository reviewRepository,
                            UserRepository userRepository) {

        this.reportRepository = reportRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/review/{id}")
    public String reportReview(@PathVariable Long id,
                               @RequestParam String reason,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               @RequestHeader(value = "Referer",
                                       required = false) String referer) {

        Review review = reviewRepository.findById(id).orElse(null

        if (review == null) {
            return "redirect:/";
        }

        User reporter = userRepository.findByUsername(authentication.getName()

        if (reporter == null) {
            return "redirect:/login";
        }

        // impedir autoreport
        if (review.getUser().getId()
                .equals(reporter.getId())) {

            return "redirect:/";
        }

        Report report = new Report(

        report.setReporterId(reporter.getId()
        report.setReviewId(review.getId()
        report.setReportedUserId(review.getUser().getId()
        report.setReason(reason
        report.setStatus(ReportStatus.PENDING

        reportRepository.save(report

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Reseña reportada correctamente."
        

        return "redirect:" + referer;
    }

    // Abrir review reportada + asignar moderador
    @GetMapping("/{id}/review")
    public String openReportedReview(@PathVariable Long id,
                                     Authentication authentication) {

        Report report = reportRepository.findById(id)
                .orElse(null

        if (report == null || report.getReviewId() == null) {
            return "redirect:/dashboard";
        }

        User moderator = userRepository
                .findByUsername(authentication.getName()

        if (moderator == null) {
            return "redirect:/login";
        }

        // si ya está cerrado no tocar
        if (report.getStatus() == ReportStatus.RESOLVED
                || report.getStatus() == ReportStatus.REJECTED) {

            return "redirect:/dashboard";
        }

        // asignar automáticamente si nadie lo tiene
        if (report.getModeratorId() == null) {

            report.setModeratorId(moderator.getId()

            report.setStatus(ReportStatus.UNDER_REVIEW

            reportRepository.save(report
        }

        // impedir que otro moderador lo robe
        else if (!report.getModeratorId().equals(moderator.getId())) {

            return "redirect:/dashboard";
        }

        return "redirect:/reviews/" + report.getReviewId(
    }

    // Rechazar reporte
    @PostMapping("/{id}/reject")
    public String rejectReport(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        Report report = reportRepository.findById(id)
                .orElse(null

        if (report == null) {
            return "redirect:/dashboard";
        }

        report.setStatus(ReportStatus.REJECTED

        reportRepository.save(report

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Reporte rechazado correctamente."
        

        return "redirect:/dashboard";
    }

    // Eliminar reseña y resolver TODOS los reportes asociados a esa reseña
    // Eliminar reseña y resolver los reportes válidos asociados
    @PostMapping("/{id}/delete-review")
    public String deleteReviewFromReport(@PathVariable Long id) {

        Report currentReport = reportRepository
                .findById(id)
                .orElseThrow(

        if (currentReport.getReviewId() == null) {
            return "redirect:/dashboard";
        }

        Review review = reviewRepository
                .findById(currentReport.getReviewId())
                .orElse(null

        if (review != null) {

            // buscar todos los reportes de esa review
            List<Report> relatedReports =
                    reportRepository.findByReviewId(review.getId()

            // resolver solo los NO rechazados
            for (Report report : relatedReports) {

                // no tocar los rechazados
                if (report.getStatus() != ReportStatus.REJECTED) {

                    report.setStatus(ReportStatus.RESOLVED
                }

                // desvincular igualmente
                report.setReviewId(null
            }

            // guardar cambios
            reportRepository.saveAll(relatedReports

            // borrar review
            reviewRepository.delete(review
        }

        return "redirect:/dashboard";
    }

    // Asignar reporte a moderador
    /*@PostMapping("/{id}/assign")
    @ResponseBody
    public boolean assignReport(@PathVariable Long id,
                                Authentication authentication) {

        Report report = reportRepository.findById(id)
                .orElse(null

        if (report == null) {
            return false;
        }

        // no asignar reportes ya cerrados
        if (report.getStatus() == ReportStatus.RESOLVED
                || report.getStatus() == ReportStatus.REJECTED) {

            return false;
        }

        User moderator = userRepository
                .findByUsername(authentication.getName()

        if (moderator == null) {
            return false;
        }

        // si ya está asignado a otro moderador
        if (report.getModeratorId() != null
                && !report.getModeratorId().equals(moderator.getId())) {

            return false;
        }

        // asignar moderador
        report.setModeratorId(moderator.getId()

        // persistir UNDER_REVIEW
        if (report.getStatus() == ReportStatus.PENDING) {

            report.setStatus(ReportStatus.UNDER_REVIEW
        }

        reportRepository.save(report

        return true;
    }*/
}