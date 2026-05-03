package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.list.MediaList;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.service.MediaListService;
import com.mara.tfgcine.service.ReviewService;
import com.mara.tfgcine.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final MediaListService mediaListService;
    private final ReviewService reviewService;

    public UserController(UserService userService,
                          MediaListService mediaListService,
                          ReviewService reviewService) {
        this.userService = userService;
        this.mediaListService = mediaListService;
        this.reviewService = reviewService;
    }

    @GetMapping("/perfil")
    public String profile(Model model, Authentication auth) {

        String username = auth.getName(

        User user = userService.findByUsername(username

        List<MediaList> lists = mediaListService.getListsByUsername(username

        model.addAttribute("user", user
        model.addAttribute("lists", lists

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

        return "tv".equals(mediaType)
                ? "redirect:/series/" + mediaId
                : "redirect:/peliculas/" + mediaId;
    }
}