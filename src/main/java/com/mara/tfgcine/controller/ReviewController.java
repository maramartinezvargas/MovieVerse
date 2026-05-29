package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.ReviewResponseDTO;
import com.mara.tfgcine.model.review.Review;
import com.mara.tfgcine.repository.ReviewRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controlador encargado de la gestión de reseñas locales de usuarios.
 *
 * <p>Permite editar, eliminar y consultar reseñas almacenadas en la base de datos,
 * siempre verificando que el usuario autenticado sea el propietario de la reseña.</p>
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    /**
     * Elimina una reseña local (es decir, de la bd de Movieverse - De TMDB no se puede)
     *
     * Comprueba que la reseña exista y que el usuario autenticado sea su propietario
     * antes de eliminarla. *La eliminación de reseñas procedentes de TMDB no se gestiona
     * por que no pueden eliminarse.
     *
     * @param id identificador de la reseña a eliminar
     * @param authentication información del usuario autenticado
     * @param referer URL de la página anterior, usada para redirigir tras la operación
     * @param redirectAttributes atributos flash para mostrar mensajes de éxito
     * @return redirección a la página anterior o a la raíz si ocurre un error o no hay permiso
     */
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

        // Validación de que el usuario autenticado es el propietario de la reseña antes de eliminarla
        String loggedUsername = authentication.getName(
        if (!review.getUser().getUsername().equals(loggedUsername)) {
            return "redirect:/";
        }

        reviewRepository.delete(review
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Reseña eliminada correctamente."
        
        return "redirect:" + referer;
    }

    /**
     * Edita una reseña local.
     *
     * Comprueba que la reseña exista y que el usuario autenticado sea su propietario
     * antes de actualizar el comentario y la puntuación. *No se gestiona la edición de
     * las reseñas de TMDB porque no se puede.
     *
     * @param id identificador de la reseña a editar
     * @param comment nuevo comentario de la reseña
     * @param rating nueva puntuación de la reseña
     * @param authentication información del usuario autenticado
     * @param referer URL de la página anterior, usada para redirigir tras la operación
     * @param redirectAttributes atributos flash para mostrar mensajes de éxito
     * @return redirección a la página anterior o a la raíz si ocurre un error o no hay permiso
     */
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


    /**
     * Obtiene una reseña por su identificador.
     *
     * Se utiliza principalmente para cargar los datos de una reseña en el modal
     * de edición.
     *
     * @param id identificador de la reseña
     * @return un objeto ReviewResponseDTO con los datos de la reseña, o null si no existe
     */
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