package com.mara.tfgcine.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mara.tfgcine.client.TmdbClient;
import com.mara.tfgcine.model.media.Media;
import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.TvSeries;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TmdbService {

    private final TmdbClient tmdbClient;
    private final ObjectMapper mapper = new ObjectMapper(

    private Map<Integer, String> movieGenreMap = new HashMap<>(
    private Map<Integer, String> tvGenreMap = new HashMap<>(

    private static final String IMG = "https://image.tmdb.org/t/p/w500";
    private static final String BACKDROP = "https://image.tmdb.org/t/p/original";

    public TmdbService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    @PostConstruct
    public void loadGenres() throws Exception {
        loadGenreMap(tmdbClient.getMovieGenres(), movieGenreMap
        loadGenreMap(tmdbClient.getTvGenres(), tvGenreMap
    }

    private void loadGenreMap(String genreJson, Map<Integer, String> genreMap) throws Exception {
        JsonNode genres = mapper.readTree(genreJson).path("genres"
        for (JsonNode g : genres) {
            genreMap.put(g.path("id").asInt(), g.path("name").asText()
        }
    }

    /* Buscador multi - Tanto para peliculas como para series */
    public List<Media> search(String query) throws Exception {

        if (query == null || query.isBlank()) {
            return Collections.emptyList(
        }

        String json = tmdbClient.searchMulti(query
        JsonNode results = mapper.readTree(json).path("results"

        List<Media> list = new ArrayList<>(

        for (JsonNode node : results) {

            String mediaType = node.path("media_type").asText(

            if ("movie".equals(mediaType)) {
                list.add(createMovieFromNode(node, node.path("id").asInt(), true)
            }

            if ("tv".equals(mediaType)) {
                list.add(createMovieFromNode(node, node.path("id").asInt(), false)
            }
        }

        String normalizedQuery = query.toLowerCase().trim(
        String[] words = normalizedQuery.split("\\s+"

        return list.stream()
                .filter(item -> {
                    if (item.title == null) return false;

                    String title = item.title.toLowerCase(

                    return Arrays.stream(words)
                            .allMatch(title::contains
                })
                .limit(10)
                .toList(
    }


    /* TOP TRENDING MOVIES --------------------------------------------------------- */

    public List<Movie> getTopMovies() throws Exception {
        return processMovieResults(tmdbClient.getTrendingMovies(), true
    }

    public List<Movie> getTrendingNowMovies() throws Exception {
        return processMovieResults(tmdbClient.getTrendingNowMovies(), true
    }

    public List<Movie> getNowPlayingMovies() throws Exception {
        return processMovieResults(tmdbClient.getNowPlayingMovies(), true
    }

    public List<Movie> discoverMovies() throws Exception {
        return processMovieResults(tmdbClient.discoverMovies(), true
    }

    public List<Movie> getBestMoviesThisYear() throws Exception {
        return processMovieResults(tmdbClient.getBestMoviesThisYear(), true
    }

    // procesa resultados comunes para películas y series, con lógica de título en inglés si el original contiene caracteres no latinos
    private List<Movie> processMovieResults(String jsonResponse, boolean isMovie) throws Exception {
        JsonNode results = mapper.readTree(jsonResponse).path("results"
        List<Movie> movies = new ArrayList<>(

        for (JsonNode node : results) {
            Movie m = new Movie(
            m.id = node.path("id").asInt(

            String title = node.path(isMovie ? "title" : "name").asText(
            if (containsNonLatin(title)) {
                String english = isMovie ? getEnglishMovieTitle(m.id) : getEnglishTvTitle(m.id
                if (english != null) title = english;
            }
            m.title = title;

            if (!node.path("poster_path").isNull()) {
                m.posterPath = IMG + node.path("poster_path").asText(
            }

            if (!node.path("backdrop_path").isNull()) {
                m.backdropPath = BACKDROP + node.path("backdrop_path").asText(
            }

            m.voteAverage = node.path("vote_average").asDouble(
            m.voteCount = node.path("vote_count").asInt(
            m.genres = extractGenres(node, isMovie

            movies.add(m
        }

        return movies;
    }

    public List<TvSeries> processTvResults(String jsonResponse) throws Exception {

        JsonNode results = mapper.readTree(jsonResponse).path("results"
        List<TvSeries> series = new ArrayList<>(

        for (JsonNode node : results) {

            TvSeries tv = new TvSeries(
            tv.id = node.path("id").asInt(

            String title = node.path("name").asText(
            if (containsNonLatin(title)) {
                String english = getEnglishTvTitle(tv.id
                if (english != null) title = english;
            }
            tv.title = title;

            if (!node.path("poster_path").isNull()) {
                tv.posterPath = IMG + node.path("poster_path").asText(
            }

            tv.voteAverage = node.path("vote_average").asDouble(
            tv.voteCount = node.path("vote_count").asInt(
            tv.genres = extractGenres(node, false

            series.add(tv
        }

        return series;
    }

    // Confort content
    public List<Media> getComfortContent() throws Exception {

        List<Media> combined = new ArrayList<>(

        combined.addAll(getComfortMovies()
        combined.addAll(getComfortSeries()

        Collections.shuffle(combined // mezcla real

        return combined.stream().limit(20).toList(
    }

    public List<Media> getComfortSeries() throws Exception {
        return new ArrayList<>(processTvResults(tmdbClient.getComfortTv())
    }

    public List<Media> getComfortMovies() throws Exception {
        return new ArrayList<>(processMovieResults(tmdbClient.getComfortMovies(), true)
    }

    private List<String> extractGenres(JsonNode node, boolean isMovie) {
        List<String> genres = new ArrayList<>(
        Map<Integer, String> genreMap = isMovie ? movieGenreMap : tvGenreMap;

        for (JsonNode gid : node.path("genre_ids")) {
            int id = gid.asInt(
            if (genreMap.containsKey(id)) {
                genres.add(genreMap.get(id)
            }
        }
        return genres;
    }

    public Movie getFeaturedMovie() {

        try {

            JsonNode results = mapper.readTree(tmdbClient.getTrendingMovies()).path("results"
            if (results.isEmpty()) return null;

            JsonNode best = null;
            double bestScore = -1;

            for (JsonNode item : results) {

                if (!"movie".equals(item.path("media_type").asText())) continue;
                if (item.path("backdrop_path").isNull()) continue;

                double voteAverage = item.path("vote_average").asDouble(0.0
                int voteCount = item.path("vote_count").asInt(0

                if (voteCount < 500) continue;

                if (voteAverage > bestScore) {
                    bestScore = voteAverage;
                    best = item;
                }
            }


            if (best == null) {
                best = results.get(0
            }

            int movieId = best.path("id").asInt(
            Movie featured = createMovieFromNode(best, movieId, true

            JsonNode details = mapper.readTree(tmdbClient.getMovieDetails(movieId)
            featured.overview = details.path("overview").asText(

            return featured;

        } catch (Exception e) {
            e.printStackTrace(
            return null;
        }
    }



    private Movie createMovieFromNode(JsonNode node, int id, boolean isMovie) {
        Movie m = new Movie(
        m.id = id;
        m.mediaType = isMovie ? "movie" : "tv";

        String title = node.path(isMovie ? "title" : "name").asText(
        if (containsNonLatin(title)) {
            String english = isMovie ? getEnglishMovieTitle(id) : getEnglishTvTitle(id
            if (english != null) title = english;
        }
        m.title = title;

        if (!node.path("poster_path").isNull()) {
            m.posterPath = IMG + node.path("poster_path").asText(
        }

        if (!node.path("backdrop_path").isNull()) {
            m.backdropPath = BACKDROP + node.path("backdrop_path").asText(
        }

        m.voteAverage = node.path("vote_average").asDouble(
        m.voteCount = node.path("vote_count").asInt(
        m.genres = extractGenres(node, isMovie

        return m;
    }

    /*  TOP TRENDING SERIES --------------------------------------------------------- */

    public List<Movie> getTrendingNowSeries() throws Exception {
        return processMovieResults(tmdbClient.getTrendingNowTv(), false
    }

    public List<Movie> getTopSeries() throws Exception {

        String jsonResponse = tmdbClient.getTrendingTv(
        JsonNode results = mapper.readTree(jsonResponse).path("results"

        List<Movie> series = new ArrayList<>(

        int currentYear = java.time.Year.now().getValue(

        for (JsonNode node : results) {

            String firstAirDate = node.path("first_air_date").asText(

            // ignorar si no tiene fecha
            if (firstAirDate == null || firstAirDate.isBlank()) continue;

            int year;
            try {
                year = Integer.parseInt(firstAirDate.substring(0, 4)
            } catch (Exception e) {
                continue;
            }

            // Filtro: solo series recientes (último año)
            if (year < currentYear - 1) continue;

            Movie m = new Movie(
            m.id = node.path("id").asInt(

            String title = node.path("name").asText(
            if (containsNonLatin(title)) {
                String english = getEnglishTvTitle(m.id
                if (english != null) title = english;
            }
            m.title = title;

            if (!node.path("poster_path").isNull()) {
                m.posterPath = IMG + node.path("poster_path").asText(
            }

            if (!node.path("backdrop_path").isNull()) {
                m.backdropPath = BACKDROP + node.path("backdrop_path").asText(
            }

            m.voteAverage = node.path("vote_average").asDouble(
            m.voteCount = node.path("vote_count").asInt(
            m.genres = extractGenres(node, false

            series.add(m
        }

        return series.stream().limit(20).toList(
    }

    public List<Movie> getUpcomingSeries() throws Exception {
        return processMovieResults(tmdbClient.getOnTheAirTv(), false
    }

    public List<Movie> getPopularSeries() throws Exception {
        return processMovieResults(tmdbClient.getPopularTv(), false
    }

    /* =========================
       HELPERS
       ========================= */

    private boolean containsNonLatin(String text) {
        if (text == null) return true;
        return text.matches(".*[\\p{IsCyrillic}\\p{IsHan}\\p{IsArabic}\\p{IsHangul}].*"
    }

    private String getEnglishMovieTitle(int movieId) {
        try {
            JsonNode json = mapper.readTree(tmdbClient.getMovieDetails(movieId)
            String title = json.path("title").asText(
            return (title != null && !title.isBlank()) ? title : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getEnglishTvTitle(int tvId) {
        try {
            JsonNode json = mapper.readTree(tmdbClient.getTvDetailsEnglish(tvId)
            String title = json.path("name").asText(
            return (title != null && !title.isBlank()) ? title : null;

        } catch (Exception e) {
            return null;
        }
    }

    public Movie getMovieDetails(int movieId) throws Exception {
        JsonNode json = mapper.readTree(tmdbClient.getMovieDetails(movieId)

        Movie m = new Movie(
        m.id = json.path("id").asInt(
        m.title = json.path("title").asText(
        m.overview = json.path("overview").asText(

        if (!json.path("poster_path").isNull()) {
            m.posterPath = IMG + json.path("poster_path").asText(
        }

        if (!json.path("backdrop_path").isNull()) {
            String bestBackdrop = getBestBackdrop(movieId

            if (bestBackdrop != null) {
                m.backdropPath = bestBackdrop;
            } else if (!json.path("backdrop_path").isNull()) {
                m.backdropPath = BACKDROP + json.path("backdrop_path").asText(
            }

        }

        m.voteAverage = json.path("vote_average").asDouble(
        m.voteCount = json.path("vote_count").asInt(
        m.releaseDate = json.path("release_date").asText(

        List<String> genres = new ArrayList<>(
        for (JsonNode g : json.path("genres")) {
            genres.add(g.path("name").asText()
        }

        m.genres = genres;

        // fecha formateada a dd/MM/yyyy
        m.releaseDate = m.getFormattedReleaseDate(

        return m;
    }

    private String getBestBackdrop(int movieId) {

        try {

            JsonNode images = mapper.readTree(tmdbClient.getMovieImages(movieId)
            JsonNode backdrops = images.path("backdrops"

            if (backdrops.isEmpty()) {
                return null;
            }

            // Seleccionar el backdrop con mayor vote_average (mejor votado), luego con  más votos,  y por ultimo con mayor resolución (mayor width)
            JsonNode bestBackdrop = null;
            double highestVote = -1;

            for (JsonNode b : backdrops) {
                double vote = b.path("vote_count").asDouble(0
                double average = b.path("vote_average").asDouble(0
                int width = b.path("width").asInt(0

                if (vote > highestVote ||
                        (vote == highestVote && average > bestBackdrop.path("vote_average").asDouble(0)) ||
                        (vote == highestVote && average == bestBackdrop.path("vote_average").asDouble(0) && width > bestBackdrop.path("width").asInt(0))) {
                    bestBackdrop = b;
                    highestVote = vote;
                }
            }

            if (bestBackdrop != null && !bestBackdrop.path("file_path").isNull()) {
                return BACKDROP + bestBackdrop.path("file_path").asText(
            }

            return null;

        } catch (Exception e) {
            // Si hay error, retornar null para usar el fallback
            return null;
        }
    }

    public List<Movie> getRelatedMovies(int movieId) throws Exception {
        try {
            String jsonResponse = tmdbClient.getMovieRecommendations(movieId
            JsonNode results = mapper.readTree(jsonResponse).path("results"

            // si recommendations está vacío, usar similar como fallback
            if (results.isEmpty()) {
                jsonResponse = tmdbClient.getMovieSimilar(movieId
                results = mapper.readTree(jsonResponse).path("results"
            }

            List<Movie> movies = new ArrayList<>(

            // Limitar a 20 películas relacionadas
            int count = 0;
            for (JsonNode node : results) {
                if (count >= 20) break;

                Movie m = new Movie(
                m.id = node.path("id").asInt(

                String title = node.path("title").asText(
                if (containsNonLatin(title)) {
                    String english = getEnglishMovieTitle(m.id
                    if (english != null) title = english;
                }
                m.title = title;

                if (!node.path("poster_path").isNull()) {
                    m.posterPath = IMG + node.path("poster_path").asText(
                }

                if (!node.path("backdrop_path").isNull()) {
                    m.backdropPath = BACKDROP + node.path("backdrop_path").asText(
                }

                m.voteAverage = node.path("vote_average").asDouble(
                m.voteCount = node.path("vote_count").asInt(
                m.genres = extractGenres(node, true

                movies.add(m
                count++;
            }

            return movies;
        } catch (Exception e) {
            System.err.println("Error al obtener películas relacionadas para ID " + movieId + ": " + e.getMessage()
            e.printStackTrace(
            return new ArrayList<>(
        }
    }
}
