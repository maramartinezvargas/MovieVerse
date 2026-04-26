package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.media.TvSeries;
import com.mara.tfgcine.model.media.Provider;
import com.mara.tfgcine.service.ReviewService;
import com.mara.tfgcine.service.TmdbService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Controller
public class SerieController {

    private final TmdbService tmdbService;
    private final ReviewService reviewService;

    public SerieController(TmdbService tmdbService, ReviewService reviewService) {
        this.tmdbService = tmdbService;
        this.reviewService = reviewService;
    }

    @GetMapping("/series/{id}")
    public String serieDetails(@PathVariable int id, Model model, HttpServletRequest request) throws Exception {

        // Serie principal
        TvSeries series = tmdbService.getSerieDetails(id
        model.addAttribute("series", series

        // Relacionadas
        model.addAttribute("relatedSeries", tmdbService.getRelatedSeries(id)

        // Reparto
        model.addAttribute("cast", tmdbService.getSerieCast(id)

        // Providers (igual que en movies pero método distinto)
        List<Provider> providers = tmdbService.getProvidersForSeries(id
        model.addAttribute("providers", providers

        // Crew
        Map<String, String> crew = tmdbService.getSerieCrewInfo(id

        model.addAttribute("directors", crew.get("directors")
        model.addAttribute("writers", crew.get("writers")
        model.addAttribute("composer", crew.get("composer")
        model.addAttribute("cinematography", crew.get("cinematography")

        // Reviews (igual que en movies)
        var reviews = reviewService.getAllReviews((long) id, "tv"
        model.addAttribute("reviews", reviews

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

        return "serie";
    }


}