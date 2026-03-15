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
    public String home(Model model) throws Exception {

        model.addAttribute("featured", tmdbService.getFeaturedMovie()

        model.addAttribute("movies", tmdbService.getTopMovies()

        model.addAttribute("tvShows", tmdbService.getTopSeries()

        model.addAttribute("nowPlayingMovies", tmdbService.getNowPlayingMovies()

        model.addAttribute("upcomingSeries", tmdbService.getUpcomingSeries()

        model.addAttribute("discoverMovies", tmdbService.discoverMovies() // DESCUBRE ALGO NUEVO

        model.addAttribute("bestMoviesYear", tmdbService.getBestMoviesThisYear()

        int lastYear = java.time.LocalDate.now().getYear() - 1;
        model.addAttribute("lastYear", lastYear

        return "index";
    }


}