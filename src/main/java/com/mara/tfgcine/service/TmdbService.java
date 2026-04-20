package com.mara.tfgcine.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mara.tfgcine.client.TmdbClient;
import com.mara.tfgcine.model.CastMember;
import com.mara.tfgcine.model.dto.ReviewDTO;
import com.mara.tfgcine.model.media.Media;
import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.Provider;
import com.mara.tfgcine.model.media.TvSeries;
import com.mara.tfgcine.model.review.TmdbReview;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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

    /* Search ---------------------------------------------------------------------------- */
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
                list.add(createTvFromNode(node, node.path("id").asInt())
            }
        }

        String normalizedQuery = query.toLowerCase().trim(
        String[] words = normalizedQuery.split("\\s+"

        return list.stream()
                .filter(item -> {
                    if (item.getTitle() == null) return false;
                    String title = item.getTitle().toLowerCase(
                    return Arrays.stream(words).allMatch(title::contains
                })
                .limit(10)
                .toList(
    }

    /* Peliculas ---------------------------------------------------------------------------- */

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

    private List<Movie> processMovieResults(String jsonResponse, boolean isMovie) throws Exception {
        JsonNode results = mapper.readTree(jsonResponse).path("results"
        List<Movie> movies = new ArrayList<>(

        for (JsonNode node : results) {
            movies.add(createMovieFromNode(node, node.path("id").asInt(), isMovie)
        }

        return movies;
    }

    /* Series ---------------------------------------------------------------------------- */

    public List<TvSeries> processTvResults(String jsonResponse) throws Exception {

        JsonNode results = mapper.readTree(jsonResponse).path("results"
        List<TvSeries> series = new ArrayList<>(

        for (JsonNode node : results) {
            series.add(createTvFromNode(node, node.path("id").asInt())
        }

        return series;
    }

    public List<TvSeries> getTrendingNowSeries() throws Exception {
        return processTvResults(tmdbClient.getTrendingNowTv()
    }

    public List<TvSeries> getUpcomingSeries() throws Exception {
        return processTvResults(tmdbClient.getOnTheAirTv()
    }

    /* Pelis y series combinadas ---------------------------------------------------------------------------- */

    public List<Media> getComfortContent() throws Exception {

        List<Media> combined = new ArrayList<>(

        combined.addAll(getComfortMovies()
        combined.addAll(getComfortSeries()

        Collections.shuffle(combined

        return combined.stream().limit(20).toList(
    }

    public List<Media> getComfortSeries() throws Exception {
        return new ArrayList<>(processTvResults(tmdbClient.getComfortTv())
    }

    public List<Media> getComfortMovies() throws Exception {
        return new ArrayList<>(processMovieResults(tmdbClient.getComfortMovies(), true)
    }

    /* Featured (Pelicula que sale en Hero / home)---------------------------------------------------- */

    public Movie getFeaturedMovie() {

        try {

            JsonNode results = mapper.readTree(tmdbClient.getTrendingMovies()).path("results"
            if (results.isEmpty()) return null;

            List<JsonNode> candidates = new ArrayList<>(

            for (JsonNode item : results) {

                if (!"movie".equals(item.path("media_type").asText())) continue;
                if (item.path("backdrop_path").isNull()) continue;

                double voteAverage = item.path("vote_average").asDouble(0.0
                int voteCount = item.path("vote_count").asInt(0

                // filtros de calidad
                if (voteCount < 300) continue;
                if (voteAverage < 6.5) continue;

                candidates.add(item
            }

            // fallback si no hay candidatos
            if (candidates.isEmpty()) {
                for (JsonNode item : results) {
                    if (!item.path("backdrop_path").isNull()) {
                        candidates.add(item
                    }
                }
            }

            if (candidates.isEmpty()) {
                JsonNode first = results.get(0
                if (first == null) return null;

                int movieId = first.path("id").asInt(
                return createMovieFromNode(first, movieId, true
            }

            // rotación por tiempo (cada hora cambia la película destacada)
            long interval =  1000 * 60 * 60 * 1;
            int index = (int) ((System.currentTimeMillis() / interval) % candidates.size()

            JsonNode selected = candidates.get(index

            int movieId = selected.path("id").asInt(
            Movie featured = createMovieFromNode(selected, movieId, true

            JsonNode details = mapper.readTree(tmdbClient.getMovieDetails(movieId)

            featured.setOverview(details.path("overview").asText()
            featured.setReleaseDate(details.path("release_date").asText()
            featured.setRuntime(details.path("runtime").asInt()

            // trailer
            featured.setTrailerKey(getTrailerKey(movieId)

            return featured;

        } catch (Exception e) {
            e.printStackTrace(
            return null;
        }
    }

    /* Mappers para deserializar los Json de las peticiones a la API y naturalizar los datos ------------ */

    private Movie createMovieFromNode(JsonNode node, int id, boolean isMovie) {

        Movie m = new Movie(
        m.setId(id
        m.setMediaType(isMovie ? "movie" : "tv"

        String title = node.path(isMovie ? "title" : "name").asText(
        if (containsNonLatin(title)) {
            String english = isMovie ? getEnglishMovieTitle(id) : getEnglishTvTitle(id
            if (english != null) title = english;
        }
        m.setTitle(title
        m.setReleaseDate(node.path("release_date").asText()

        if (!node.path("poster_path").isNull()) {
            m.setPosterPath(IMG + node.path("poster_path").asText()
        }

        if (!node.path("backdrop_path").isNull()) {
            m.setBackdropPath(BACKDROP + node.path("backdrop_path").asText()
        }

        m.setVoteAverage(node.path("vote_average").asDouble()
        m.setVoteCount(node.path("vote_count").asInt()
        m.setGenres(extractGenres(node, isMovie)


        return m;
    }

    public Movie getMovieDetails(int movieId) throws Exception {

        JsonNode json = mapper.readTree(tmdbClient.getMovieDetails(movieId)

        Movie m = new Movie(
        m.setId(json.path("id").asInt()
        m.setTitle(json.path("title").asText()
        m.setOverview(json.path("overview").asText()

        if (!json.path("poster_path").isNull()) {
            m.setPosterPath(IMG + json.path("poster_path").asText()
        }

        if (!json.path("backdrop_path").isNull()) {
            String bestBackdrop = getBestBackdrop(movieId

            if (bestBackdrop != null) {
                m.setBackdropPath(bestBackdrop
            } else {
                m.setBackdropPath(BACKDROP + json.path("backdrop_path").asText()
            }
        }

        m.setVoteAverage(json.path("vote_average").asDouble()
        m.setVoteCount(json.path("vote_count").asInt()
        m.setReleaseDate(json.path("release_date").asText()

        // runtime
        m.setRuntime(json.path("runtime").asInt()

        // trailer
        m.setTrailerKey(getTrailerKey(movieId)

        List<String> genres = new ArrayList<>(
        for (JsonNode g : json.path("genres")) {
            genres.add(g.path("name").asText()
        }
        m.setGenres(genres

        List<String> companies = new ArrayList<>(

        for (JsonNode c : json.path("production_companies")) {
            companies.add(c.path("name").asText()
        }

        m.setProductionCompanies(companies

        List<String> countries = new ArrayList<>(

        for (JsonNode c : json.path("production_countries")) {
            countries.add(c.path("name").asText()
        }

        m.setProductionCountries(countries

        // idioma original
        m.setOriginalLanguage(json.path("original_language").asText()

        return m;
    }

    private String getBestBackdrop(int movieId) {
        try {
            JsonNode images = mapper.readTree(tmdbClient.getMovieImages(movieId)
            JsonNode backdrops = images.path("backdrops"

            if (backdrops.isEmpty()) return null;

            JsonNode bestBackdrop = null;
            double highestVote = -1;

            for (JsonNode b : backdrops) {
                double vote = b.path("vote_count").asDouble(0
                double average = b.path("vote_average").asDouble(0
                int width = b.path("width").asInt(0

                if (bestBackdrop == null ||
                        vote > highestVote ||
                        (vote == highestVote && average > bestBackdrop.path("vote_average").asDouble(0)) ||
                        (vote == highestVote && average == bestBackdrop.path("vote_average").asDouble(0)
                                && width > bestBackdrop.path("width").asInt(0))) {

                    bestBackdrop = b;
                    highestVote = vote;
                }
            }

            if (bestBackdrop != null && !bestBackdrop.path("file_path").isNull()) {
                return BACKDROP + bestBackdrop.path("file_path").asText(
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    public List<Movie> getRelatedMovies(int movieId) throws Exception {

        try {
            String jsonResponse = tmdbClient.getMovieRecommendations(movieId
            JsonNode results = mapper.readTree(jsonResponse).path("results"

            if (results.isEmpty()) {
                jsonResponse = tmdbClient.getMovieSimilar(movieId
                results = mapper.readTree(jsonResponse).path("results"
            }

            List<Movie> movies = new ArrayList<>(

            int count = 0;
            for (JsonNode node : results) {

                if (count >= 20) break;

                Movie m = createMovieFromNode(node, node.path("id").asInt(), true
                movies.add(m

                count++;
            }

            return movies;

        } catch (Exception e) {
            e.printStackTrace(
            return new ArrayList<>(
        }
    }


    private TvSeries createTvFromNode(JsonNode node, int id) {

        TvSeries tv = new TvSeries(
        tv.setId(id
        tv.setMediaType("tv"

        String title = node.path("name").asText(
        if (containsNonLatin(title)) {
            String english = getEnglishTvTitle(id
            if (english != null) title = english;
        }
        tv.setTitle(title
        tv.setReleaseDate(node.path("first_air_date").asText()
        if (!node.path("poster_path").isNull()) {
            tv.setPosterPath(IMG + node.path("poster_path").asText()
        }

        tv.setVoteAverage(node.path("vote_average").asDouble()
        tv.setVoteCount(node.path("vote_count").asInt()
        tv.setGenres(extractGenres(node, false)

        return tv;
    }

    /* Plataformas de streaming --------------- */
    public List<Provider> getProvidersForMovie(int movieId) {

        try {
            String json = tmdbClient.getWatchProviders(movieId

            JsonNode root = mapper.readTree(json
            JsonNode providersES = root.path("results").path("ES").path("flatrate"

            List<Provider> providers = new ArrayList<>(

            if (providersES.isArray()) {
                for (JsonNode p : providersES) {

                    String name = p.path("provider_name").asText(""
                    String logoPath = p.path("logo_path").asText(""

                    providers.add(new Provider(
                            name,
                            logoPath,
                            getProviderLink(name)
                    )
                }
            }

            return providers;

        } catch (Exception e) {
            return Collections.emptyList( // importante
        }
    }

    private String getProviderLink(String name) {
        return switch (name.toLowerCase()) {
            case "netflix" -> "https://www.netflix.com";
            case "amazon prime video" -> "https://www.primevideo.com";
            case "disney plus" -> "https://www.disneyplus.com";
            case "hbo max" -> "https://www.hbomax.com";
            case "apple tv plus" -> "https://tv.apple.com";
            case "movistar plus+" -> "https://ver.movistarplus.es";
            case "filmin" -> "https://www.filmin.es";
            case "rakuten tv" -> "https://www.rakuten.tv";
            case "google play movies" -> "https://play.google.com/store/movies";
            case "youtube" -> "https://www.youtube.com";
            default -> "#";
        };
    }

    /* Reviews *--------------------------------------------------------------------------- */
    public List<ReviewDTO> getReviews(Long movieId) {

        try {
            String json = tmdbClient.getMovieReviews(movieId.intValue()
            JsonNode root = mapper.readTree(json).path("results"

            List<ReviewDTO> list = new ArrayList<>(

            for (JsonNode node : root) {

                ReviewDTO review = new ReviewDTO(

                review.setUsername(node.path("author").asText()
                review.setComment(node.path("content").asText()
                review.setSource("TMDB"

                // fecha (parse si quieres fino, esto vale para ahora)
                review.setCreatedAt(
                        OffsetDateTime.parse(node.path("created_at").asText()).toLocalDateTime()
                

                JsonNode authorDetailsNode = node.path("author_details"

                if (!authorDetailsNode.isMissingNode()) {

                    if (!authorDetailsNode.path("rating").isNull()) {
                        review.setRating(authorDetailsNode.path("rating").asDouble()
                    }

                    String avatarPath = authorDetailsNode.path("avatar_path").asText(null
                    String avatarUrl = null;

                    if (avatarPath != null && !avatarPath.isBlank()) {

                        if (avatarPath.startsWith("/http")) {
                            avatarUrl = avatarPath.substring(1
                        } else {
                            avatarUrl = "https://image.tmdb.org/t/p/w45" + avatarPath;
                        }
                    }

                    review.setAvatarUrl(avatarUrl
                }

                list.add(review
            }

            return list;

        } catch (Exception e) {
            return Collections.emptyList(
        }
    }

    /* Cast ---------------------------------------------------------------------------- */
    public List<CastMember> getCast(int movieId) {

        try {
            String json = tmdbClient.getMovieCredits(movieId

            JsonNode root = mapper.readTree(json
            JsonNode castArray = root.path("cast"

            List<CastMember> castList = new ArrayList<>(

            int limit = Math.min(castArray.size(), 15

            for (int i = 0; i < limit; i++) {

                JsonNode actor = castArray.get(i

                String name = actor.path("name").asText(
                String character = actor.path("character").asText(

                String profilePath = actor.path("profile_path").asText(null

                if (profilePath != null && !profilePath.isBlank()) {
                    profilePath = "https://image.tmdb.org/t/p/w185" + profilePath;
                } else {
                    profilePath = null; // fallback controlado (lo manejas en Thymeleaf)
                }

                castList.add(new CastMember(name, character, profilePath)
            }

            return castList;

        } catch (Exception e) {
            e.printStackTrace(
            return Collections.emptyList(
        }
    }

    /* Crew (director, guionista, compositor, director de fotografía) -------------------------------------------------- */
    public Map<String, String> getMovieCrewInfo(int movieId) {

        try {
            String json = tmdbClient.getMovieCredits(movieId
            JsonNode crewArray = mapper.readTree(json).path("crew"

            Set<String> directors = new LinkedHashSet<>(
            Set<String> writers = new LinkedHashSet<>(
            Set<String> composers = new LinkedHashSet<>(
            Set<String> cinematographers = new LinkedHashSet<>(

            for (JsonNode person : crewArray) {

                String job = person.path("job").asText(
                String name = person.path("name").asText(

                switch (job) {
                    case "Director" -> directors.add(name
                    case "Screenplay", "Writer" -> writers.add(name
                    case "Original Music Composer" -> composers.add(name
                    case "Director of Photography" -> cinematographers.add(name
                }
            }

            Map<String, String> result = new HashMap<>(

            result.put("directors", directors.isEmpty() ? null : String.join(", ", directors)
            result.put("writers", writers.isEmpty() ? null : String.join(", ", writers)
            result.put("composer", composers.isEmpty() ? null : String.join(", ", composers)
            result.put("cinematography", cinematographers.isEmpty() ? null : String.join(", ", cinematographers)

            return result;

        } catch (Exception e) {
            return Collections.emptyMap(
        }
    }

    /* Helpers ---------------------------------------------------------------------------- */

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

    private String getTrailerKey(int movieId) {
        try {
            JsonNode json = mapper.readTree(tmdbClient.getMovieVideos(movieId)
            JsonNode results = json.path("results"

            for (JsonNode video : results) {
                String type = video.path("type").asText(
                String site = video.path("site").asText(

                if ("Trailer".equalsIgnoreCase(type) && "YouTube".equalsIgnoreCase(site)) {
                    return video.path("key").asText(
                }
            }

            // fallback → a veces no hay "Trailer", pero sí "Teaser"
            for (JsonNode video : results) {
                String site = video.path("site").asText(
                if ("YouTube".equalsIgnoreCase(site)) {
                    return video.path("key").asText(
                }
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

}