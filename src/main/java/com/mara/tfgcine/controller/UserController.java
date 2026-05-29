package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.ProfileMediaDTO;
import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.list.MediaList;
import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.TvSeries;
import com.mara.tfgcine.model.status.MediaStatus;
import com.mara.tfgcine.model.status.UserMediaStatus;
import com.mara.tfgcine.model.user.User;
import java.util.List;
import com.mara.tfgcine.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mara.tfgcine.model.dto.ProfileReviewDTO;
import com.mara.tfgcine.model.review.Review;

import java.util.ArrayList;


/**
 * Controlador encargado de la gestión del perfil de usuario.
 *
 * Permite mostrar la información del perfil, las listas del usuario, los títulos
 * marcados como vistos o pendientes, los "likes" y las reseñas propias. También
 * gestiona la creación de nuevas reseñas desde el perfil o desde la ficha de una película/serie.</p>
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
@Controller
public class UserController {

    private final UserService userService;
    private final MediaListService mediaListService;
    private final ReviewService reviewService;
    private final LikeService likeService;
    private final UserMediaStatusService userMediaStatusService;

    public UserController(UserService userService,
                          MediaListService mediaListService,
                          ReviewService reviewService,
                          LikeService likeService,
                          UserMediaStatusService userMediaStatusService) {
        this.userService = userService;
        this.mediaListService = mediaListService;
        this.reviewService = reviewService;
        this.likeService = likeService;
        this.userMediaStatusService = userMediaStatusService;
    }

    /**
     * Muestra la página de perfil del usuario autenticado.
     *
     * Recupera los datos del usuario, sus listas, sus títulos favoritos,
     * su contenido marcado como visto o por ver, y sus reseñas para mostrarlos
     * en la vista del perfil.
     *
     * @param model modelo de Spring MVC para enviar datos a la vista
     * @param auth información de autenticación del usuario actual
     * @return nombre de la vista del perfil
     * @throws Exception si ocurre un error al consultar los servicios asociados
     */
    @GetMapping("/perfil")
    public String profile(Model model, Authentication auth) throws Exception {

        String username = auth.getName(

        User user = userService.findByUsername(username

        List<MediaList> lists = mediaListService.getListsByUsername(username

        model.addAttribute("user", user
        model.addAttribute("lists", lists
        List<Like> likes = likeService.getUserLikes(user

        List<ProfileMediaDTO> likedMedia = new ArrayList<>(

        for (Like like : likes) {

            ProfileMediaDTO dto = new ProfileMediaDTO(

            dto.setMediaId(like.getMediaId()
            dto.setMediaType(like.getMediaType().name()
            dto.setCreatedAt(like.getCreatedAt()
            dto.setTitle(like.getTitle()
            dto.setPosterPath(like.getPosterPath()
            dto.setVoteAverage(like.getVoteAverage()

            likedMedia.add(dto
        }

        // Cargar listas de títulos vistos y por ver
        List<ProfileMediaDTO> watchedMedia =
                userMediaStatusService
                        .getStatusesByType(
                                user,
                                MediaStatus.WATCHED
                        )
                        .stream()
                        .map(this::mapStatusToDTO)
                        .toList(

        List<ProfileMediaDTO> watchlistMedia =
                userMediaStatusService
                        .getStatusesByType(
                                user,
                                MediaStatus.WATCHLIST
                        )
                        .stream()
                        .map(this::mapStatusToDTO)
                        .toList(

        model.addAttribute("watchedMedia", watchedMedia
        model.addAttribute("watchlistMedia", watchlistMedia
        model.addAttribute("likedMedia", likedMedia

        // Cargar reseñas del usuario
        List<ProfileReviewDTO> profileReviews = new ArrayList<>(

        for (Review review : user.getReviews()) {
            ProfileReviewDTO dto = new ProfileReviewDTO(
            dto.setId(review.getId() // Para poder eliminar la review desde el perfil
            dto.setMediaId(review.getMediaId()
            dto.setMediaType(review.getMediaType()
            dto.setComment(review.getComment()
            dto.setRating(review.getRating()
            dto.setCreatedAt(review.getCreatedAt()
            dto.setTitle(review.getTitle()
            dto.setPosterPath(review.getPosterPath()

            profileReviews.add(dto
        }

        model.addAttribute("profileReviews", profileReviews
        return "profile";
    }

    private ProfileMediaDTO mapStatusToDTO(UserMediaStatus mediaStatus) {

        ProfileMediaDTO dto = new ProfileMediaDTO(
        dto.setMediaId(mediaStatus.getMediaId()
        dto.setMediaType(mediaStatus.getMediaType().name()
        dto.setCreatedAt(mediaStatus.getCreatedAt()
        dto.setTitle(mediaStatus.getTitle()
        dto.setPosterPath(mediaStatus.getPosterPath()
        dto.setVoteAverage(mediaStatus.getVoteAverage()

        return dto;
    }


    /**
     * Crea una nueva reseña para un contenido multimedia.
     *
     * Si el usuario no está autenticado, redirige al login. En caso contrario,
     * intenta registrar la reseña y muestra un mensaje de éxito o error según el resultado.
     *
     * @param mediaId identificador del contenido multimedia
     * @param comment comentario de la reseña
     * @param rating puntuación opcional de la reseña
     * @param mediaType tipo de contenido multimedia
     * @param title título del contenido
     * @param posterPath ruta del póster del contenido
     * @param auth información de autenticación del usuario
     * @param redirectAttributes atributos flash para mostrar mensajes en la redirección
     * @return redirección a la ficha de la película o serie, o al login si el usuario no está autenticado
     */
    @PostMapping("/reviews")
    public String createReview(@RequestParam Long mediaId,
                               @RequestParam String comment,
                               @RequestParam(required = false) Integer rating,
                               @RequestParam MediaType mediaType,
                               @RequestParam String title,
                               @RequestParam String posterPath,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName(

        try {
            reviewService.createReview(username, mediaId, comment, rating, mediaType, title, posterPath
            redirectAttributes.addFlashAttribute("successMessage", "¡Gracias por tu reseña!"
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "¡Ups! Ya has reseñado este título antes."
        }

        return mediaType == MediaType.SERIE
                ? "redirect:/series/" + mediaId
                : "redirect:/peliculas/" + mediaId;
    }
}