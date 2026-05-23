package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.ReviewResponseDTO;
import com.mara.tfgcine.model.review.Review;
import com.mara.tfgcine.repository.ReviewRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Eliminar una review local (TMDB no se puede)
    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id,
                               Authentication authentication,
                               @RequestHeader(value = "Referer", required = false)
                               String referer,
                               RedirectAttributes redirectAttributes){

        Review review = reviewRepository.findById(id).orElse(null

        if (review == null) {
            return "redirect:/";
        }

        String loggedUsername = authentication.getName(

        // Seguridad: SOLO el dueño puede borrar
        if (!review.getUser().getUsername().equals(loggedUsername)) {
            return "redirect:/";
        }

        reviewRepository.delete(review
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Reseña eliminada correctamente."
        
        return "redirect:" + referer;
    }

    // Editar una review local (TMDB no se puede)
    @PostMapping("/edit/{id}")
    public String editReview(@PathVariable Long id,
                             @RequestParam String comment,
                             @RequestParam Double rating,
                             Authentication authentication,
                             @RequestHeader(value = "Referer",
                                     required = false) String referer,
                             RedirectAttributes redirectAttributes) {

        Review review =
                reviewRepository.findById(id).orElse(null

        if (review == null) {
            return "redirect:/";
        }

        String loggedUsername =
                authentication.getName(

        if (!review.getUser()
                .getUsername()
                .equals(loggedUsername)) {

            return "redirect:/";
        }

        review.setComment(comment
        review.setRating(rating.intValue()

        reviewRepository.save(review

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Reseña actualizada correctamente."
        

        return "redirect:" + referer;
    }

    // Obtener una review por su ID (para mostrar en el modal de edición)
    @GetMapping("/{id}")
    @ResponseBody
    public ReviewResponseDTO getReviewById(@PathVariable Long id) {

        Review review = reviewRepository.findById(id)
                .orElse(null

        if (review == null) {
            return null;
        }

        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getMediaId(),
                review.getMediaType().name(),
                review.getUser().getUsername()
        
    }

}