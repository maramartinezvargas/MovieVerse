package com.mara.tfgcine.controller;

import com.mara.tfgcine.service.TmdbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página de inicio que muestra las películas y series destacadas
 */
@Controller
public class HomeController {

    // Inyección del servicio de TMDB para obtener los datos
    private final TmdbService tmdbService;

    // Constructor para inyectar el servicio
    public HomeController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    // Mapeo para la ruta raíz que muestra la página de inicio
    @GetMapping("/")
    public String home(Model model) {

        // Featured (Destacada) -> Se agrega al modelo la película destacada obtenida del servicio de TMDB
        model.addAttribute("featured", tmdbService.getFeaturedMovie()

        // SECCIÓN: Tendencias
        // Se agrega al modelo la lista de películas y series populares obtenida del servicio de TMDB
        model.addAttribute("movies", tmdbService.getTopMovies()
        model.addAttribute("tvShows", tmdbService.getTopSeries()

        // SECCIÓN: Próximos estrenos
        // Se agrega al modelo la lista de películas y series próximas a estrenarse obtenida del
        model.addAttribute("nowPlayingMovies", tmdbService.getNowPlayingMovies()
        model.addAttribute("upcomingSeries", tmdbService.getUpcomingSeries()
        // Retorna el nombre de la plantilla que se va a renderizar (index.html)
        return "index";
    }
}
