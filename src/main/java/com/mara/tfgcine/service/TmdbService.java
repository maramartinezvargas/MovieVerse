package com.mara.tfgcine.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mara.tfgcine.model.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TmdbService {

    // RestTemplate inyectado para hacer llamadas HTTP a la API de TMDB
    private final RestTemplate restTemplate;

    // Constructor para inyectar el RestTemplate desde la configuración de Spring (AppConfig)
    public TmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(TmdbService.class

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base}")
    private String baseUrl;

    @Value("${tmdb.image.poster}")
    private String posterBase;

    @Value("${tmdb.image.backdrop}")
    private String backdropBase;

    @Value("${tmdb.api.language.primary}")
    private String primaryLang;

    @Value("${tmdb.api.language.fallback}")
    private String fallbackLang;

    private final ObjectMapper mapper = new ObjectMapper(

    // Cache de géneros de películas (id → name)
    private List<JsonNode> cachedGenres = null;

    // Cache de géneros de series (id → name)
    private List<JsonNode> cachedTvGenres = null;


    /* =========================
       FEATURED (HERO)
       ========================= */

    // getFeaturedMovie() obtiene la película destacada de la semana desde TMDB
    // combina dos factores:
    // - sea relevante ahora (trending)
    // - tenga una valoración fiable (mínimo de votos: >=100 ) y buena puntuación (score > bestScore)
    public Movie getFeaturedMovie() {
        try {
            String url = baseUrl + "/trending/movie/day"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang;

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            if (results.isEmpty()) return null;

            JsonNode best = null;
            double bestScore = -1;

            for (JsonNode item : results) {
                double score = item.path("vote_average").asDouble(0.0
                int voteCount = item.path("vote_count").asInt(0

                // Mínimo 100 votos para evitar películas con pocas valoraciones
                if (voteCount >= 100 && score > bestScore) {
                    bestScore = score;
                    best = item;
                }
            }

            // Fallback: si ninguna supera los 100 votos, coge la más popular
            if (best == null) {
                best = results.get(0
            }

            return mapMovie(best, true

        } catch (Exception e) {
            logger.error("Error obteniendo película destacada (featured)", e
            return null;
        }
    }

    /* PELÍCULAS MÁS POPULARES DEL MOMENTO (TENDENCIAS) */
    public List<Movie> getTopMovies() {

        List<Movie> movies = new ArrayList<>(

        try {
            String url = baseUrl + "/trending/movie/day"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang
                    + "&page=1";

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            for (int i = 0; i < 25 && i < results.size( i++) {
                movies.add(mapMovie(results.get(i), false)
            }

        } catch (Exception e) {
            logger.error("Error obteniendo top películas", e
        }

        return movies;
    }

    /* SERIES MÁS POPULARES DEL MOMENTO (TENDENCIAS) - Se filtran las series con género "News" */
    public List<Movie> getTopSeries() {

        List<Movie> seriesList = new ArrayList<>(

        try {
            String url = baseUrl + "/tv/popular"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang
                    + "&page=1";

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            for (int i = 0; i < results.size() && seriesList.size() < 25; i++) {
                Movie tv = mapTv(results.get(i)
                if (tv.getGenres() == null || !tv.getGenres().contains("News")) {
                    seriesList.add(tv
                }
            }

            // Ordenar por puntuación descendente
            seriesList.sort((a, b) -> Double.compare(b.getVoteAverage(), a.getVoteAverage())

        } catch (Exception e) {
            logger.error("Error obteniendo top series", e
        }

        return seriesList;
    }

    /* =========================
       MAPPER SERIES
       ========================= */
    private Movie mapTv(JsonNode jsonTv) {

        Movie tv = new Movie(

        /* ---------- ID ---------- */
        int id = jsonTv.path("id").asInt(
        tv.setId(id
        /* ---------- FECHA DE ESTRENO ---------- */
        tv.setReleaseDate(jsonTv.path("first_air_date").asText(null)

        /* ---------- TEMPORADA ACTIVA ---------- */
        tv.setSeasonNumber(jsonTv.path("number_of_seasons").asInt(0)

        /* ---------- TÍTULO ---------- */
        String nameEs = jsonTv.path("name").asText(
        String finalName = nameEs;

        if (containsNonLatin(nameEs)) {
            // Primero intenta obtener el título en inglés via API
            String nameEn = getEnglishTvTitle(id
            if (nameEn != null && !containsNonLatin(nameEn)) {
                finalName = nameEn;
            } else {
                // Si el inglés también tiene caracteres no latinos, usa original_name
                String originalName = jsonTv.path("original_name").asText(
                if (originalName != null && !originalName.isBlank()) {
                    finalName = originalName;
                }
            }
        }

        if (finalName == null || finalName.isBlank()) {
            finalName = "Sin título";
        }

        tv.setTitle(finalName

        /* ---------- RATING ---------- */
        tv.setVoteAverage(jsonTv.path("vote_average").asDouble(0.0)
        tv.setVoteCount(jsonTv.path("vote_count").asInt(0)

        /* ---------- POSTER ---------- */
        String poster = jsonTv.path("poster_path").asText(null
        tv.setPosterPath(
                poster != null
                        ? posterBase + poster
                        : "/img/no-poster.png"
        

        /* ---------- BACKDROP ---------- */
        String backdrop = jsonTv.path("backdrop_path").asText(null
        tv.setBackdropPath(
                backdrop != null
                        ? backdropBase + backdrop
                        : null
        

        /* ---------- GÉNEROS (TV) ---------- */
        List<String> genreNames = new ArrayList<>(
        JsonNode genreIds = jsonTv.path("genre_ids"

        if (genreIds.isArray()) {
            try {
                List<JsonNode> genres = getTvGenres(

                for (JsonNode idNode : genreIds) {
                    int gid = idNode.asInt(
                    genres.stream()
                            .filter(g -> g.path("id").asInt() == gid)
                            .findFirst()
                            .ifPresent(g -> genreNames.add(g.path("name").asText())
                }
            } catch (Exception ignored) {}
        }

        tv.setGenres(genreNames
        return tv;
    }

    /* =========================
       MAPPER PELÍCULAS
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
        

        /* ---------- FECHA DE ESTRENO ---------- */
        movie.setReleaseDate(jsonMovie.path("release_date").asText(null)

        // En mapMovie(), sustituye la línea de releaseDate por esto:
        String rawDate = jsonMovie.path("release_date").asText(null
        if (rawDate != null && !rawDate.isBlank()) {
            try {
                LocalDate date = LocalDate.parse(rawDate
                movie.setReleaseDate(date.format(
                        DateTimeFormatter.ofPattern("d MMMM, yyyy", new java.util.Locale("es", "ES"))
                )
            } catch (Exception e) {
                movie.setReleaseDate(rawDate
            }
        }



        /* ---------- BACKDROP (HERO) ---------- */
        String backdrop = jsonMovie.path("backdrop_path").asText(null
        movie.setBackdropPath(
                backdrop != null
                        ? backdropBase + backdrop
                        : null
        

        /* ---------- OVERVIEW ---------- */
        if (includeOverview) {
            movie.setOverview(jsonMovie.path("overview").asText("")
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
   EN CARTELERA - Películas
   ========================= */
    public List<Movie> getNowPlayingMovies() {

        List<Movie> movies = new ArrayList<>(

        try {
            String url = baseUrl + "/movie/now_playing"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang
                    + "&page=1";

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            for (int i = 0; i < 25 && i < results.size( i++) {
                movies.add(mapMovie(results.get(i), false)
            }

        } catch (Exception e) {
            logger.error("Error obteniendo películas en cartelera", e
        }

        return movies;
    }

    /* =========================
       PRÓXIMOS ESTRENOS - Series
       ========================= */
    public List<Movie> getUpcomingSeries() {

        List<Movie> seriesList = new ArrayList<>(

        try {
            String url = baseUrl + "/tv/on_the_air"
                    + "?api_key=" + apiKey
                    + "&language=" + primaryLang
                    + "&page=1";

            String response = restTemplate.getForObject(url, String.class
            JsonNode results = mapper.readTree(response).path("results"

            for (int i = 0; i < results.size() && seriesList.size() < 25; i++) {
                Movie tv = mapTv(results.get(i)

                // Filtrar series con género "News"
                if (tv.getGenres() == null || !tv.getGenres().contains("News")) {
                    seriesList.add(tv
                }
            }

        } catch (Exception e) {
            logger.error("Error obteniendo series en emisión", e
        }

        return seriesList;
    }

    /* =========================
       HELPERS
       ========================= */

    /**
     * Carga los géneros de películas desde TMDB con caché
     */
    private List<JsonNode> getMovieGenres() throws Exception {
        if (cachedGenres != null) return cachedGenres;

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
     * Carga los géneros de series desde TMDB con caché
     */
    private List<JsonNode> getTvGenres() throws Exception {
        if (cachedTvGenres != null) return cachedTvGenres;

        String url = baseUrl + "/genre/tv/list"
                + "?api_key=" + apiKey
                + "&language=" + primaryLang;

        String response = restTemplate.getForObject(url, String.class
        JsonNode genresNode = mapper.readTree(response).path("genres"

        cachedTvGenres = new ArrayList<>(
        genresNode.forEach(cachedTvGenres::add
        return cachedTvGenres;
    }

    /**
     * Fallback a inglés para títulos de películas con caracteres no latinos
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
     * Fallback a inglés para títulos de series con caracteres no latinos
     */
    private String getEnglishTvTitle(int tvId) {
        try {
            String url = baseUrl + "/tv/" + tvId
                    + "?api_key=" + apiKey
                    + "&language=" + fallbackLang;

            String response = restTemplate.getForObject(url, String.class
            JsonNode json = mapper.readTree(response

            String titleEn = json.path("name").asText(
            return titleEn != null && !titleEn.isBlank() ? titleEn : null;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Detecta si el texto contiene caracteres no latinos (cirílico, chino, árabe...)
     */
    private boolean containsNonLatin(String text) {
        if (text == null) return true;
        return text.matches(".*[^\\p{IsLatin}0-9\\s:,'\"\\-\\.].*"
    }
}