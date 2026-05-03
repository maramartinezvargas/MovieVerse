package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.list.MediaList;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.service.MediaListService;
import com.mara.tfgcine.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final MediaListService mediaListService;

    public UserController(UserService userService,
                          MediaListService mediaListService) {
        this.userService = userService;
        this.mediaListService = mediaListService;
    }

    @GetMapping("/perfil")
    public String profile(Model model, Authentication auth) {

        String username = auth.getName(

        User user = userService.findByUsername(username

        // ✅ ahora sí
        List<MediaList> lists = mediaListService.getListsByUsername(username

        model.addAttribute("user", user
        model.addAttribute("lists", lists

        return "profile";
    }
}