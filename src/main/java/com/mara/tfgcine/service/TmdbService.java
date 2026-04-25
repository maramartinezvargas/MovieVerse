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

import java.text.Normalizer;
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

        String normalizedQuery = normalize(query
        String[] words = normalizedQuery.split("\\s+"

        return list.stream()
                .filter(item -> {
                    if (item.getTitle() == null) return false;
                    String title = normalize(item.getTitle()
                    return Arrays.stream(words).anyMatch(title::contains
                })
                .sorted((a, b) -> {
                    String ta = normalize(a.getTitle()
                    String tb = normalize(b.getTitle()

                    return Integer.compare(
                            score(tb, words, normalizedQuery),
                            score(ta, words, normalizedQuery)
                    
                })
                .limit(50)
                .toList(
    }

    private int score(String title, String[] words, String fullQuery) {
        int score = 0;

        // MATCH EXACTO
        if (title.equals(fullQuery)) {
            return 1000;
        }

        // CONTIENE FRASE COMPLETA
        if (title.contains(fullQuery)) {
            score += 200;
        }

        // empieza por la query
        if (title.startsWith(fullQuery)) {
            score += 150;
        }

        // palabras sueltas
        for (String word : words) {
            if (title.contains(word)) {
                score += 20;
            }
        }

        return score;
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
            featured.setTrailerKey(getTrailerKey(movieId, true)

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
        m.setTrailerKey(getTrailerKey(movieId, true)
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
            JsonNode es = root.path("results").path("ES"

            List<Provider> providers = new ArrayList<>(
            Set<Integer> seen = new HashSet<>( // ✅ aquí

            addProviders(providers, es.path("flatrate"), seen
            addProviders(providers, es.path("buy"), seen
            addProviders(providers, es.path("rent"), seen
            addProviders(providers, es.path("free"), seen
            addProviders(providers, es.path("ads"), seen

            return providers;

        } catch (Exception e) {
            return Collections.emptyList(
        }
    }

    /* Enlaza el ID del proveedor con su web oficial. TMDB no proporciona el link directo, solo el ID y el nombre, así que hacemos esta asociación manualmente para los proveedores más comunes en España. */
    private String getProviderLink(int id) {
        return switch (id) {
            case 8, 1796 -> "https://www.netflix.com";
            case 119, 10, 2100, 1825, 1968,  528, 2243 -> "https://www.primevideo.com/";
            case 337 -> "https://www.disneyplus.com";
            case 384, 1899 -> "https://www.hbomax.com";
            case 2, 350, 1854 -> "https://tv.apple.com";
            case 149, 2241 -> "https://ver.movistarplus.es";
            case 63, 64 -> "https://www.filmin.es";
            case 35 -> "https://www.rakuten.tv";
            case 1773 -> "https://www.skyshowtime.com";
            case 283 -> "https://www.crunchyroll.com";
            case 62 -> "https://www.atresplayer.com";
            case 541 -> "https://www.rtve.es/play/";
            case 1838 -> "https://www.tivify.tv/";
            case 538 -> "https://watch.plex.tv/es";
            default -> null;
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

    /* SERIES ---------------------------------------------------------------------------- */
    public TvSeries getSerieDetails(int tvId) throws Exception {

        JsonNode json = mapper.readTree(tmdbClient.getTvDetails(tvId)

        TvSeries tv = new TvSeries(

        tv.setId(json.path("id").asInt()
        tv.setTitle(json.path("name").asText()
        tv.setOverview(json.path("overview").asText()

        if (!json.path("poster_path").isNull()) {
            tv.setPosterPath(IMG + json.path("poster_path").asText()
        }

        if (!json.path("backdrop_path").isNull()) {
            tv.setBackdropPath(BACKDROP + json.path("backdrop_path").asText()
        }

        tv.setVoteAverage(json.path("vote_average").asDouble()
        tv.setVoteCount(json.path("vote_count").asInt()

        tv.setReleaseDate(json.path("first_air_date").asText()

        // trailer
        tv.setTrailerKey(getTrailerKey(tvId, false)

        // géneros
        List<String> genres = new ArrayList<>(
        for (JsonNode g : json.path("genres")) {
            genres.add(g.path("name").asText()
        }
        tv.setGenres(genres

        // production companies
        List<String> companies = new ArrayList<>(
        for (JsonNode c : json.path("production_companies")) {
            companies.add(c.path("name").asText()
        }
        tv.setProductionCompanies(companies

        // production countries
        List<String> countries = new ArrayList<>(
        for (JsonNode c : json.path("production_countries")) {
            countries.add(c.path("name").asText()
        }
        tv.setProductionCountries(countries
        tv.setOriginalLanguage(json.path("original_language").asText()

        tv.setNumberOfSeasons(json.path("number_of_seasons").asInt()
        tv.setNumberOfEpisodes(json.path("number_of_episodes").asInt()
        // status (Ended / Returning Series)
        tv.setStatus(json.path("status").asText()

        // duración episodios
        JsonNode runtimes = json.path("episode_run_time"

        if (runtimes.isArray() && runtimes.size() > 0) {
            tv.setEpisodeRuntime(runtimes.get(0).asInt()
        }

        return tv;
    }

    public List<CastMember> getSerieCast(int tvId) {

        try {
            String json = tmdbClient.getTvCredits(tvId

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
                    profilePath = null;
                }

                castList.add(new CastMember(name, character, profilePath)
            }

            return castList;

        } catch (Exception e) {
            return Collections.emptyList(
        }
    }

    public List<TvSeries> getRelatedSeries(int tvId) throws Exception {

        try {
            String jsonResponse = tmdbClient.getTvRecommendations(tvId
            JsonNode results = mapper.readTree(jsonResponse).path("results"

            if (results.isEmpty()) {
                jsonResponse = tmdbClient.getTvSimilar(tvId
                results = mapper.readTree(jsonResponse).path("results"
            }

            List<TvSeries> series = new ArrayList<>(

            int count = 0;
            for (JsonNode node : results) {

                if (count >= 20) break;

                TvSeries tv = createTvFromNode(node, node.path("id").asInt()
                series.add(tv

                count++;
            }

            return series;

        } catch (Exception e) {
            return new ArrayList<>(
        }
    }

    public List<Provider> getProvidersForSeries(int tvId) {

        try {
            String json = tmdbClient.getTvWatchProviders(tvId

            JsonNode root = mapper.readTree(json
            JsonNode es = root.path("results").path("ES"

            List<Provider> providers = new ArrayList<>(

            // flatrate
            JsonNode flatrate = es.path("flatrate"
            if (flatrate.isArray()) {
                for (JsonNode p : flatrate) {
                    providers.add(new Provider(
                            p.path("provider_id").asInt(),
                            p.path("provider_name").asText(""),
                            p.path("logo_path").asText(""),
                            getProviderLink(p.path("provider_id").asInt())
                    )
                }
            }

            // free (RTVE, Tivify, etc)
            JsonNode free = es.path("free"
            if (free.isArray()) {
                for (JsonNode p : free) {
                    providers.add(new Provider(
                            p.path("provider_id").asInt(),
                            p.path("provider_name").asText(""),
                            p.path("logo_path").asText(""),
                            getProviderLink(p.path("provider_id").asInt())
                    )
                }
            }

            // ads
            JsonNode ads = es.path("ads"
            if (ads.isArray()) {
                for (JsonNode p : ads) {
                    providers.add(new Provider(
                            p.path("provider_id").asInt(),
                            p.path("provider_name").asText(""),
                            p.path("logo_path").asText(""),
                            getProviderLink(p.path("provider_id").asInt())
                    )
                }
            }

            return providers;

        } catch (Exception e) {
            return Collections.emptyList(
        }
    }

    public Map<String, String> getSerieCrewInfo(int tvId) {

        try {
            String json = tmdbClient.getTvCredits(tvId
            JsonNode crewArray = mapper.readTree(json).path("crew"

            Set<String> creators = new LinkedHashSet<>(

            for (JsonNode person : crewArray) {
                String job = person.path("job").asText(
                if ("Creator".equalsIgnoreCase(job)) {
                    creators.add(person.path("name").asText()
                }
            }

            Map<String, String> result = new HashMap<>(
            result.put("directors", creators.isEmpty() ? null : String.join(", ", creators)
            result.put("writers", null
            result.put("composer", null
            result.put("cinematography", null

            return result;

        } catch (Exception e) {
            return Collections.emptyMap(
        }
    }

    public List<ReviewDTO> getSerieReviews(Long tvId) {

        try {
            String json = tmdbClient.getTvReviews(tvId.intValue()
            JsonNode root = mapper.readTree(json).path("results"

            List<ReviewDTO> list = new ArrayList<>(

            for (JsonNode node : root) {

                ReviewDTO review = new ReviewDTO(

                review.setUsername(node.path("author").asText()
                review.setComment(node.path("content").asText()
                review.setSource("TMDB"

                review.setCreatedAt(
                        OffsetDateTime.parse(node.path("created_at").asText()).toLocalDateTime()
                

                JsonNode authorDetailsNode = node.path("author_details"

                if (!authorDetailsNode.isMissingNode()) {

                    if (!authorDetailsNode.path("rating").isNull()) {
                        review.setRating(authorDetailsNode.path("rating").asDouble()
                    }

                    String avatarPath = authorDetailsNode.path("avatar_path").asText(null

                    if (avatarPath != null && !avatarPath.isBlank()) {

                        if (avatarPath.startsWith("/http")) {
                            review.setAvatarUrl(avatarPath.substring(1)
                        } else {
                            review.setAvatarUrl("https://image.tmdb.org/t/p/w45" + avatarPath
                        }
                    }
                }

                list.add(review
            }

            return list;

        } catch (Exception e) {
            return Collections.emptyList(
        }
    }

    /* Método genérico para obtener trailer tanto de película como de serie */
    private String getTrailerKey(int id, boolean isMovie) {

        try {
            String jsonStr = isMovie
                    ? tmdbClient.getMovieVideos(id)
                    : tmdbClient.getTvVideos(id

            JsonNode results = mapper.readTree(jsonStr).path("results"

            JsonNode fallback = null;

            for (JsonNode video : results) {

                String type = video.path("type").asText(
                String site = video.path("site").asText(
                String name = video.path("name").asText().toLowerCase(
                String lang = video.path("iso_639_1").asText(

                if (!lang.equals("es") && !lang.equals("en")) continue;

                if (!"YouTube".equalsIgnoreCase(site)) continue;

                // PRIORIDAD 1 → Trailer real
                if ("Trailer".equalsIgnoreCase(type)) {
                    return video.path("key").asText(
                }

                // PRIORIDAD 2 → algo que tenga pinta de trailer
                if (fallback == null && name.contains("trailer")) {
                    fallback = video;
                }
            }

            // fallback inteligente
            if (fallback != null) {
                return fallback.path("key").asText(
            }



        } catch (Exception e) {
            return null;
        }

        return null;
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

    // Quitar tildes, carácteres especiales y normalizar a minúsculas para mejorar la búsqueda (ej: "Amélie" → "amelie")
    private String normalize(String text) {
        if (text == null) return "";

        text = text.toLowerCase( // normalizar a minúsculas

        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // quitar tildes
                .replaceAll("[^a-z0-9\\s]", "") // quitar caracteres especiales (dejando solo letras, números y espacios)
                .trim(
    }


    // Método genérico para añadir proveedores de cualquier categoría (flatrate, buy, rent, free, ads)
    private void addProviders(List<Provider> list, JsonNode array, Set<Integer> seen){
        if (array != null && array.isArray()) {
            for (JsonNode p : array) {

                int providerId = p.path("provider_id").asInt(
                if (seen.contains(providerId)) continue;

                seen.add(providerId

                list.add(new Provider(
                        providerId,
                        p.path("provider_name").asText(""),
                        p.path("logo_path").asText(""),
                        getProviderLink(providerId)
                )
            }
        }
    }
}