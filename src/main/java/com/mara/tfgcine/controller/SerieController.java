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


/**
 * Controlador encargado de la gestión de series.
 *
 * Proporciona las vistas y endpoints necesarios para mostrar el detalle de una serie,
 * explorar el catálogo de series y cargar páginas adicionales.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
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

    /**
     * Muestra la página de detalle de una serie.
     *
     * Carga la información principal de la serie, el reparto, los proveedores,
     * el equipo técnico, las reseñas, los "likes" y el estado de visualización del usuario
     * si está autenticado.
     *
     * @param id identificador de la serie en TMDB
     * @param model modelo de Spring MVC para enviar datos a la vista
     * @param request petición HTTP actual, usada para obtener la URL actual
     * @return nombre de la vista de detalle de serie
     * @throws Exception si ocurre un error al consultar los servicios externos
     */
    @GetMapping("/series/{id}")
    public String serieDetails(@PathVariable int id, Model model, HttpServletRequest request) throws Exception {

        // Serie principal
        TvSeries series = tmdbService.getSerieDetails(id
        model.addAttribute("series", series

        // Relacionadas
        model.addAttribute("relatedSeries", tmdbService.getRelatedSeries(id)

        // Reparto
        model.addAttribute("cast", tmdbService.getSerieCast(id)

        // Providers
        List<Provider> providers = tmdbService.getProvidersForSeries(id
        model.addAttribute("providers", providers

        // Crew
        Map<String, String> crew = tmdbService.getSerieCrewInfo(id

        model.addAttribute("directors", crew.get("directors")
        model.addAttribute("writers", crew.get("writers")
        model.addAttribute("composer", crew.get("composer")
        model.addAttribute("cinematography", crew.get("cinematography")

        List<ReviewDTO> reviews = reviewService.getAllReviews((long) id, MediaType.SERIE

        // REORDENAR: mostrar review del usuario autenticado, si la hay, arriba siempre
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


    /**
     * Muestra la página de exploración de series.
     *
     * Recupera series paginadas, géneros disponibles y una lista de años
     * para construir los filtros de la vista.
     *
     * @param page número de página a mostrar
     * @param model modelo de Spring MVC para enviar datos a la vista
     * @return nombre de la vista de exploración de series
     * @throws Exception si ocurre un error al consultar los servicios externos
     */
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

    /**
     * Devuelve una página de series en formato JSON para carga dinámica.
     *
     * Permite filtrar por año, género, ordenación, puntuación mínima y número mínimo
     * de votos.
     *
     * @param page número de página a recuperar
     * @param year año de estreno opcional
     * @param genre género opcional
     * @param sort criterio de ordenación opcional
     * @param minRating puntuación mínima opcional
     * @param minVotes número mínimo de votos opcional
     * @return lista de series filtradas
     * @throws Exception si ocurre un error al consultar los servicios externos
     */
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