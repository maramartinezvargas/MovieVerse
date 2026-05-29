package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.media.Media;
import com.mara.tfgcine.service.TmdbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Controlador de la página de inicio y búsqueda de contenido multimedia *******************************
 *
 * Gestiona la carga de la página de inicio y expone un endpoint de búsqueda
 * para consultar contenido multimedia (películas y series) a través del servicio TMDB.</p>
 *
 * @author Tamara Martínez Vargas
 * @since 02/06/2026
 * @version 28/05/2026
 */
@Controller
public class HomeController {

    // Instancia del servicio de TMDB para obtener datos de películas y series
    private final TmdbService tmdbService;

    // Constructor que inyecta el servicio de TMDB
    public HomeController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    /**
     * Realiza una búsqueda de contenido multimedia (películas y series).
     *
     * Endpoint REST utilizado por el buscador para devolver resultados
     * en formato JSON.
     *
     * @param query texto de búsqueda introducido por el usuario
     * @return lista de resultados de tipo Media
     * @throws Exception si ocurre un error al consultar el servicio de TMDB
     */
    @GetMapping("/api/search")
    @ResponseBody
    public List<Media> search(@RequestParam String query) throws Exception {
        return tmdbService.search(query
    }

    /**
     * Carga la página principal con contenido destacado y secciones dinámicas.
     *
     * @param model modelo de Spring MVC para enviar datos a la vista
     * @return nombre de la vista principal (`index`)
     * @throws Exception si ocurre un error al recuperar datos desde TMDB
     */
    @GetMapping("/")
    public String home(Model model) throws Exception {

        // Contenido destacado (Hero)
        model.addAttribute("featured", tmdbService.getFeaturedMovie()

        // Series en tendencia actual
        model.addAttribute("tvShows", tmdbService.getTrendingNowSeries()

        // Películas en cartelera
        model.addAttribute("nowPlayingMovies", tmdbService.getNowPlayingMovies()

        // Series en emisión
        model.addAttribute("upcomingSeries", tmdbService.getUpcomingSeries()

        // Películas mejor valoradas del último año
        model.addAttribute("bestMoviesYear", tmdbService.getBestMoviesThisYear()

        // Año anterior usado en la sección "mejor valoradas del último año"
        int lastYear = java.time.LocalDate.now().getYear() - 1;
        model.addAttribute("lastYear", lastYear

        // Películas en tendencia actual
        model.addAttribute("movies", tmdbService.getTrendingNowMovies()

        // Contenido de confort (que nunca fallan / tanto películas como series)
        model.addAttribute("comfortContent", tmdbService.getComfortContent()

        return "index";
    }
}