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

@Controller
public class UserController {

    private final UserService userService;
    private final MediaListService mediaListService;
    private final ReviewService reviewService;
    private final LikeService likeService;
    private final TmdbService tmdbService;
    private final UserMediaStatusService userMediaStatusService;

    public UserController(UserService userService,
                          MediaListService mediaListService,
                          ReviewService reviewService,
                          LikeService likeService,
                          TmdbService tmdbService,
                          UserMediaStatusService userMediaStatusService) {
        this.userService = userService;
        this.mediaListService = mediaListService;
        this.reviewService = reviewService;
        this.likeService = likeService;
        this.tmdbService = tmdbService;
        this.userMediaStatusService = userMediaStatusService;
    }

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

            try {

                if (review.getMediaType() == MediaType.MOVIE) {
                    Movie movie = tmdbService.getMovieDetails(
                            review.getMediaId().intValue()
                    

                    if (movie != null) {
                        dto.setTitle(movie.getTitle()
                        dto.setPosterPath(movie.getPosterPath()
                    }
                } else {
                    TvSeries serie = tmdbService.getSerieDetails(
                            review.getMediaId().intValue()
                    

                    if (serie != null) {
                        dto.setTitle(serie.getTitle()
                        dto.setPosterPath(serie.getPosterPath()
                    }
                }
            } catch (Exception e) {
                dto.setTitle("Título no disponible"
            }
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

    // Manejar creación de reviews
    @PostMapping("/reviews")
    public String createReview(@RequestParam Long mediaId,
                               @RequestParam String comment,
                               @RequestParam(required = false) Integer rating,
                               @RequestParam MediaType mediaType,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName(

        try {
            reviewService.createReview(username, mediaId, comment, rating, mediaType
            redirectAttributes.addFlashAttribute("successMessage", "¡Gracias por tu reseña!"
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "¡Ups! Ya has reseñado este título antes."
        }

        return mediaType == MediaType.SERIE
                ? "redirect:/series/" + mediaId
                : "redirect:/peliculas/" + mediaId;
    }
}