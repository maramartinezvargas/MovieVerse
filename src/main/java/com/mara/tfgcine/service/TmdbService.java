package com.mara.tfgcine.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mara.tfgcine.model.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TmdbService {

    private static final Logger logger = LoggerFactory.getLogger(TmdbService.class

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base}")
    private String baseUrl;

    // Bases de imágenes
    @Value("${tmdb.image.poster}")
    private String posterBase;

    @Value("${tmdb.image.backdrop}")
    private String backdropBase;

    // Idiomas
    @Value("${tmdb.api.language.primary}")
    private String primaryLang;

    @Value("${tmdb.api.language.fallback}")
    private String fallbackLang;

    private final RestTemplate restTemplate = new RestTemplate(
    private final ObjectMapper mapper = new ObjectMapper(

    // Cache de géneros (id → name)
    private List<JsonNode> cachedGenres = null;

    /* =========================
       FEATURED (HERO)
       ========================= */

    /**
     * Película destacada:
     * - Top trending del día (TMDB)
     * - Usada en la sección HERO
     */
    public Movie getFeaturedMovie() {
        try {
            String url = baseUrl + "/trending/movie/day"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang;

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            if (results.isEmpty()) {
                return null;
            }

            return mapMovie(results.get(0), true

        } catch (Exception e) {
            logger.error("Error obteniendo película destacada (featured)", e
            return null;
        }
    }

    /* =========================
       TOP peliculas en tendencia
       ========================= */

    public List<Movie> getTopMovies() {

        List<Movie> movies = new ArrayList<>(

        try {
            String url = baseUrl + "/movie/popular"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang
                    + "&page=1";

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            for (int i = 0; i < 25 && i < results.size( i++) {
                movies.add(mapMovie(results.get(i), false)
            }

        } catch (Exception e) {
            logger.error("Error obteniendo top 5 películas", e
        }

        return movies;
    }

    /* Top Series en tendencia
       ========================= */
    public List<Movie> getTopSeries() {

        List<Movie> seriesList = new ArrayList<>(

        try {
            String url = baseUrl + "/tv/popular"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang
                    + "&page=1";

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            for (int i = 0; i < 25 && i < results.size( i++) {
                seriesList.add(mapMovie(results.get(i), false)
            }

        } catch (Exception e) {
            logger.error("Error obteniendo top 5 series", e
        }

        return seriesList;
    }

    /* =========================
       MAPPER CENTRAL
       ========================= */

    private Movie mapMovie(JsonNode jsonMovie, boolean includeOverview) {

        Movie movie = new Movie(

        int movieId = jsonMovie.path("id").asInt(
        movie.setId(movieId

        /* ---------- TÍTULO ---------- */

        String titleEs = jsonMovie.path("title").asText(
        String finalTitle = titleEs;

        if (containsNonLatin(titleEs)) {
            String titleEn = getEnglishTitle(movieId
            if (titleEn != null) {
                finalTitle = titleEn;
            }
        }

        if (finalTitle == null || finalTitle.isBlank()) {
            finalTitle = jsonMovie.path("original_title").asText("Sin título"
        }

        movie.setTitle(finalTitle

        /* ---------- RATING ---------- */

        movie.setVoteAverage(jsonMovie.path("vote_average").asDouble(0.0)
        movie.setVoteCount(jsonMovie.path("vote_count").asInt(0)

        /* ---------- POSTER ---------- */

        String poster = jsonMovie.path("poster_path").asText(null
        movie.setPosterPath(
                poster != null
                        ? posterBase + poster
                        : "/img/no-poster.png"
        

        /* ---------- BACKDROP (HERO) ---------- */

        String backdrop = jsonMovie.path("backdrop_path").asText(null
        movie.setBackdropPath(
                backdrop != null
                        ? backdropBase + backdrop
                        : null
        

        /* ---------- OVERVIEW ---------- */

        if (includeOverview) {
            movie.setOverview(
                    jsonMovie.path("overview").asText("")
            
        }

        /* ---------- GÉNEROS ---------- */

        List<String> genreNames = new ArrayList<>(
        JsonNode genreIds = jsonMovie.path("genre_ids"

        if (genreIds.isArray()) {
            try {
                List<JsonNode> genres = getMovieGenres(

                for (JsonNode idNode : genreIds) {
                    int id = idNode.asInt(
                    genres.stream()
                            .filter(g -> g.path("id").asInt() == id)
                            .findFirst()
                            .ifPresent(g -> genreNames.add(g.path("name").asText())
                }
            } catch (Exception e) {
                logger.warn("No se pudieron resolver los géneros", e
            }
        }

        movie.setGenres(genreNames

        return movie;
    }

    /* =========================
       HELPERS
       ========================= */

    /**
     * Carga y cachea los géneros de películas (TMDB)
     */
    private List<JsonNode> getMovieGenres() throws Exception {

        if (cachedGenres != null) {
            return cachedGenres;
        }

        String url = baseUrl + "/genre/movie/list"
                + "?api_key=" + apiKey
                + "&language=" + primaryLang;

        String response = restTemplate.getForObject(url, String.class
        JsonNode genresNode = mapper.readTree(response).path("genres"

        cachedGenres = new ArrayList<>(
        genresNode.forEach(cachedGenres::add

        return cachedGenres;
    }

    /**
     * Fallback a título en inglés si el español contiene caracteres no latinos
     */
    private String getEnglishTitle(int movieId) {
        try {
            String url = baseUrl + "/movie/" + movieId
                    + "?api_key=" + apiKey
                    + "&language=" + fallbackLang;

            String response = restTemplate.getForObject(url, String.class
            JsonNode json = mapper.readTree(response

            String titleEn = json.path("title").asText(
            return titleEn != null && !titleEn.isBlank() ? titleEn : null;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Detecta caracteres no latinos (chino, japonés, cirílico, etc.)
     */
    private boolean containsNonLatin(String text) {
        if (text == null) return true;
        return text.matches(".*[^\\p{IsLatin}0-9\\s:,'\"\\-\\.].*"
    }
}
