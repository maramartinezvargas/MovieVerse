package com.mara.tfgcine.controller;

import com.mara.tfgcine.service.TmdbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final TmdbService tmdbService;

    public HomeController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/")
    public String home(Model model) {

        // HERO
        model.addAttribute("featured", tmdbService.getFeaturedMovie()

        // SECCIÓN: Top populares
        model.addAttribute("movies", tmdbService.getTopMovies()

        // SECCIÓN: Top series
        model.addAttribute("tvShows", tmdbService.getTopSeries()

        return "index";
    }
}
