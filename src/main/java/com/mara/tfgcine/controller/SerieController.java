package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.dto.ReviewDTO;
import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.media.TvSeries;
import com.mara.tfgcine.model.media.Provider;
import com.mara.tfgcine.model.status.UserMediaStatus;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.service.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SerieController {

    private final TmdbService tmdbService;
    private final ReviewService reviewService;
    private final LikeService likeService;
    private final UserService userService;
    private final UserMediaStatusService userMediaStatusService;

    public SerieController(TmdbService tmdbService,
                           ReviewService reviewService,
                           LikeService likeService,
                           UserService userService,
                           UserMediaStatusService userMediaStatusService) {

        this.tmdbService = tmdbService;
        this.reviewService = reviewService;
        this.likeService = likeService;
        this.userService = userService;
        this.userMediaStatusService = userMediaStatusService;
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

        List<ReviewDTO> reviews = reviewService.getAllReviews((long) id, MediaType.SERIE

        // REORDENAR: review del usuario arriba
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {

            String username = auth.getName(

            List<ReviewDTO> userReview = reviews.stream()
                    .filter(r -> username.equals(r.getUsername()))
                    .toList(

            List<ReviewDTO> others = reviews.stream()
                    .filter(r -> !username.equals(r.getUsername()))
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .toList(

            List<ReviewDTO> finalList = new ArrayList<>(
            finalList.addAll(userReview
            finalList.addAll(others

            reviews = finalList;
        }

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

        // Likes (para mostrar el estado del botón de like)
        boolean liked = false;
        int totalLikes = likeService.countLikes((long) id, MediaType.SERIE

        Authentication authLike = SecurityContextHolder.getContext().getAuthentication(

        if (authLike != null && authLike.isAuthenticated() && !authLike.getName().equals("anonymousUser")) {

            String username = authLike.getName(
            User user = userService.findByUsername(username

            liked = likeService.hasUserLiked(user, (long) id, MediaType.SERIE
        }

        model.addAttribute("liked", liked
        model.addAttribute("totalLikes", totalLikes

        // Estado de la película para el usuario (visto, por ver, ninguno)
        UserMediaStatus currentStatus = null;

        // Solo se carga el estado si el usuario está autenticado
        if (auth != null
                && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {

            User user =
                    userService.findByUsername(
                            auth.getName()
                    

            currentStatus = userMediaStatusService
                    .getStatus(
                            user,
                            (long) series.getId(),
                            MediaType.SERIE
                    )
                    .orElse(null
        }

        model.addAttribute("currentStatus", currentStatus

        return "serie";
    }

    /* ================= EXPLORAR SERIES ================= */

    @GetMapping("/series")
    public String explorarSeries(@RequestParam(defaultValue = "1") int page, Model model) throws Exception {

        List<TvSeries> series = new ArrayList<>(

        for (int i = 1; i <= page; i++) {
            series.addAll(tmdbService.discoverSeriesWithPoster(i)
        }

        model.addAttribute("series", series
        model.addAttribute("currentPage", page
        model.addAttribute("genres", tmdbService.getTvGenresMap()

        List<Integer> years = new ArrayList<>(
        int currentYear = java.time.Year.now().getValue(

        for (int i = currentYear; i >= 1900; i--) {
            years.add(i
        }

        model.addAttribute("years", years

        return "series";
    }


    /* ================= API SERIES ================= */

    @GetMapping("/api/series")
    @ResponseBody
    public List<TvSeries> getSeriesPage(
            @RequestParam int page,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer genre,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minVotes
    ) throws Exception {

        return tmdbService.discoverSeriesFiltered(page, year, genre, sort, minRating, minVotes
    }

}