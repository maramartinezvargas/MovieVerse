package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.Provider;
import com.mara.tfgcine.service.TmdbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MovieController {

    private final TmdbService tmdbService;

    public MovieController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }


    @GetMapping("/peliculas/{id}")
    public String movieDetails(@PathVariable int id, Model model) throws Exception {

        Movie movie = tmdbService.getMovieDetails(id
        model.addAttribute("movie", movie

        model.addAttribute("relatedMovies", tmdbService.getRelatedMovies(id)
        model.addAttribute("cast", tmdbService.getCast(id)

        List<Provider> providers = tmdbService.getProvidersForMovie(id
        model.addAttribute("providers", providers

        return "movie";
    }

}
