package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.media.Media;
import com.mara.tfgcine.service.TmdbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    private final TmdbService tmdbService;

    public HomeController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    // Buscador (multi - tanto para peliculas como para series)
    @GetMapping("/api/search")
    @ResponseBody
    public List<Media> search(@RequestParam String query) throws Exception {
        return tmdbService.search(query
    }

    @GetMapping("/")
    public String home(Model model) throws Exception {

        // Destacada (Hero)
        model.addAttribute("featured", tmdbService.getFeaturedMovie()

        // Series más vistas ahora
        model.addAttribute("tvShows", tmdbService.getTrendingNowSeries()

        // En cartelera
        model.addAttribute("nowPlayingMovies", tmdbService.getNowPlayingMovies()

        // En emisión
        model.addAttribute("upcomingSeries", tmdbService.getUpcomingSeries()

        // Mejor valoradas
        model.addAttribute("bestMoviesYear", tmdbService.getBestMoviesThisYear()

        // Peliculas más vistas ahora
        model.addAttribute("movies", tmdbService.getTrendingNowMovies()

        // Confort pelis y series
        model.addAttribute("comfortContent", tmdbService.getComfortContent()

        int lastYear = java.time.LocalDate.now().getYear() - 1;
        model.addAttribute("lastYear", lastYear

        return "index";
    }
}