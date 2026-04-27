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
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

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
    public String movieDetails(@PathVariable int id, Model model, HttpServletRequest request) throws Exception {

        Movie movie = tmdbService.getMovieDetails(id
        model.addAttribute("movie", movie

        model.addAttribute("relatedMovies", tmdbService.getRelatedMovies(id)
        model.addAttribute("cast", tmdbService.getMovieCast(id)

        List<Provider> providers = tmdbService.getProvidersForMovie(id
        model.addAttribute("providers", providers

        // Datos detalle
        Map<String, String> crew = tmdbService.getMovieCrewInfo(id

        model.addAttribute("directors", crew.get("directors")
        model.addAttribute("writers", crew.get("writers")
        model.addAttribute("composer", crew.get("composer")
        model.addAttribute("cinematography", crew.get("cinematography")

        // Obtener reviews (locales + TMDB)
        var reviews = reviewService.getAllReviews((long) id, "movie"
        model.addAttribute("reviews", reviews

        // Calcular rating promedio
        double avgRating = reviews.stream()
                .filter(r -> r.getRating() != null)
                .filter(r -> "LOCAL".equals(r.getSource()))
                .mapToDouble(r -> r.getRating())
                .average()
                .orElse(0.0

        long localReviewCount = reviews.stream()
                .filter(r -> "LOCAL".equals(r.getSource()))
                .count(

        model.addAttribute("localReviewCount", localReviewCount
        model.addAttribute("avgRating", avgRating
        model.addAttribute("reviewCount", reviews.size()
        model.addAttribute("currentUrl", request.getRequestURI()
        return "movie";
    }

    @PostMapping("/reviews")
    public String createReview(@RequestParam Long mediaId,
                               @RequestParam String comment,
                               @RequestParam Integer rating,
                               @RequestParam String mediaType) {

        reviewService.createReview(mediaId, comment, rating, mediaType

        if ("tv".equals(mediaType)) {
            return "redirect:/series/" + mediaId;
        }

        return "redirect:/peliculas/" + mediaId;
    }
}