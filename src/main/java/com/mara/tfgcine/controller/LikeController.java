package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.like.MediaType;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.service.LikeService;
import com.mara.tfgcine.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;

    public LikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    @PostMapping("/toggle")
    public Map<String, Object> toggleLike(
            @RequestParam Long mediaId,
            @RequestParam MediaType mediaType,
            Principal principal
    ) {

        // Usuario no logueado
        if (principal == null) {
            return Map.of("error", "Usuario no autenticado"
        }

        User user = userService.findByUsername(principal.getName()

        boolean liked = likeService.toggleLike(user, mediaId, mediaType
        int totalLikes = likeService.countLikes(mediaId, mediaType

        return Map.of(
                "liked", liked,
                "totalLikes", totalLikes
        
    }
}