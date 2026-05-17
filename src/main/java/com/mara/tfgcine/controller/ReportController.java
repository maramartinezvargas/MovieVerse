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

        Review review =
                reviewRepository.findById(id).orElse(null

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
                "success",
                "Reseña reportada correctamente."
        

        return "redirect:" + referer;
    }
}