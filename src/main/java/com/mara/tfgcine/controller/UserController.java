package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.LikedMediaDTO;
import com.mara.tfgcine.model.like.Like;
import com.mara.tfgcine.model.like.MediaType;
import com.mara.tfgcine.model.list.MediaList;
import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.TvSeries;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final MediaListService mediaListService;
    private final ReviewService reviewService;
    private final LikeService likeService;
    private final TmdbService tmdbService;

    public UserController(UserService userService,
                          MediaListService mediaListService,
                          ReviewService reviewService,
                          LikeService likeService,
                          TmdbService tmdbService) {
        this.userService = userService;
        this.mediaListService = mediaListService;
        this.reviewService = reviewService;
        this.likeService = likeService;
        this.tmdbService = tmdbService;
    }

    @GetMapping("/perfil")
    public String profile(Model model, Authentication auth) throws Exception {

        String username = auth.getName(

        User user = userService.findByUsername(username

        List<MediaList> lists = mediaListService.getListsByUsername(username

        model.addAttribute("user", user
        model.addAttribute("lists", lists
        List<Like> likes = likeService.getUserLikes(user

        List<LikedMediaDTO> likedMedia = new ArrayList<>(

        for (Like like : likes) {

            LikedMediaDTO dto = new LikedMediaDTO(

            dto.setMediaId(like.getMediaId()
            dto.setMediaType(like.getMediaType().name()
            dto.setCreatedAt(like.getCreatedAt()

            if (like.getMediaType() == MediaType.MOVIE) {

                Movie movie = tmdbService.getMovieDetails(
                        like.getMediaId().intValue()
                

                dto.setTitle(movie.getTitle()
                dto.setPosterPath(movie.getPosterPath()
                dto.setVoteAverage(movie.getVoteAverage()

            } else {

                TvSeries serie = tmdbService.getSerieDetails(
                        like.getMediaId().intValue()
                

                dto.setTitle(serie.getTitle()
                dto.setPosterPath(serie.getPosterPath()
                dto.setVoteAverage(serie.getVoteAverage()
            }

            likedMedia.add(dto
        }

        model.addAttribute("likedMedia", likedMedia

        return "profile";
    }

    // Manejar creación de reviews
    @PostMapping("/reviews")
    public String createReview(@RequestParam Long mediaId,
                               @RequestParam String comment,
                               @RequestParam(required = false) Integer rating,
                               @RequestParam String mediaType,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName(

        try {
            reviewService.createReview(username, mediaId, comment, rating, mediaType
            redirectAttributes.addFlashAttribute("success", "¡Gracias por tu reseña!"
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "¡Ups! Ya has reseñado este título antes."
        }

        return MediaType.SERIE.name().equalsIgnoreCase(mediaType)
                ? "redirect:/series/" + mediaId
                : "redirect:/peliculas/" + mediaId;
    }
}