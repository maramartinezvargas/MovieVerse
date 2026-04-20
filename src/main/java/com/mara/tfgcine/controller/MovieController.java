package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.Provider;
import com.mara.tfgcine.service.ReviewService;
import com.mara.tfgcine.service.TmdbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MovieController {

    private final TmdbService tmdbService;
    private final ReviewService reviewService;

    // Constructor con ambas dependencias
    public MovieController(TmdbService tmdbService, ReviewService reviewService) {
        this.tmdbService = tmdbService;
        this.reviewService = reviewService;
    }

    // Detalles película
    @GetMapping("/peliculas/{id}")
    public String movieDetails(@PathVariable int id, Model model) throws Exception {

        Movie movie = tmdbService.getMovieDetails(id
        model.addAttribute("movie", movie

        model.addAttribute("relatedMovies", tmdbService.getRelatedMovies(id)
        model.addAttribute("cast", tmdbService.getCast(id)

        List<Provider> providers = tmdbService.getProvidersForMovie(id
        model.addAttribute("providers", providers

        // Obtener reviews (locales + TMDB)
        var reviews = reviewService.getAllReviews((long) id
        model.addAttribute("reviews", reviews

        // Calcular rating promedio
        double avgRating = reviews.stream()
                .filter(r -> r.getRating() != null)
                .mapToDouble(r -> r.getRating())
                .average()
                .orElse(0.0

        model.addAttribute("avgRating", avgRating
        model.addAttribute("reviewCount", reviews.size()

        return "movie";
    }

    @PostMapping("/reviews")
    public String createReview(@RequestParam Long mediaId,
                               @RequestParam String comment,
                               @RequestParam Integer rating) {

        reviewService.createReview(mediaId, comment, rating

        return "redirect:/peliculas/" + mediaId;
    }
}