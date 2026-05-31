package com.mara.tfgcine.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mara.tfgcine.client.TmdbClient;
import com.mara.tfgcine.model.media.CastMember;
import com.mara.tfgcine.model.dto.ReviewDTO;
import com.mara.tfgcine.model.media.Media;
import com.mara.tfgcine.model.media.Movie;
import com.mara.tfgcine.model.media.Provider;
import com.mara.tfgcine.model.media.TvSeries;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Servicio que encapsula las llamadas a la API de TMDB y transforma las respuestas JSON
 * en modelos de la aplicación (Movie, TvSeries, Provider, ReviewDTO, etc.).
 *
 * Funcionalidades principales:
 * - Delega solicitudes HTTP a {@link com.mara.tfgcine.client.TmdbClient}.
 * - Mappea JSON de TMDB, adapta los datos obtenidos a los objetos de la aplicación (cargando
 *   géneros al arrancar) y facilita búsqueda, exploración, detalles, reparto, proveedores
 *   y reseñas.
 *
 * Notas:
 * - Usa {@code tmdb.image.poster} y {@code tmdb.image.backdrop} como base para las URLs de imágenes.
 * - Algunos métodos lanzan {@code Exception}; otros devuelven colecciones vacías como fallback -> devolver
 *   un valor por defecto (lista vacía) cuando falla la llamada a TMDB, para que no rompa la app
 *
 * @see com.mara.tfgcine.client.TmdbClient
 * @author Tamara Martínez
 * @since 02/03/2026
 * @version 28/05/2026
 */

@Service
public class TmdbService {

    private final TmdbClient tmdbClient;
    private final ObjectMapper mapper = new ObjectMapper(

    private Map<Integer, String> movieGenreMap = new HashMap<>(
    private Map<Integer, String> tvGenreMap = new HashMap<>(

    @Value("${tmdb.image.poster}")
    private String posterBaseUrl;

    @Value("${tmdb.image.backdrop}")
    private String backdropBaseUrl;

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


    /**
     * Busca películas y series por título usando la API de TMDB.
     *
     * Filtra resultados sin póster/backdrop, normaliza títulos y ordena por
     * relevancia según coincidencia exacta, contiene frase, comienza con la query, etc.
     *
     * @param query término de búsqueda
     * @return lista de hasta 50 medios ordenados por relevancia
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
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
                Media m = createMovieFromNode(node, node.path("id").asInt(), true
                if (hasVisual(m)) list.add(m

            } else if ("tv".equals(mediaType)) {
                Media tv = createTvFromNode(node, node.path("id").asInt()
                if (hasVisual(tv)) list.add(tv
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

    // Comprobar si tiene poster el titulo para priorizar resultados visuales en el buscador
    private boolean hasVisual(Media m) {
        return (m.getPosterPath() != null && !m.getPosterPath().isBlank())
                || (m.getBackdropPath() != null && !m.getBackdropPath().isBlank()
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

    /**
     * Obtiene las películas en tendencia actual desde TMDB.
     *
     * @return lista de películas
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
    public List<Movie> getTopMovies() throws Exception {
        return processMovieResults(tmdbClient.getTrendingMovies(), true
    }

    public List<Movie> getTrendingNowMovies() throws Exception {
        return processMovieResults(tmdbClient.getTrendingNowMovies(), true
    }

    public List<Movie> getNowPlayingMovies() throws Exception {
        return processMovieResults(tmdbClient.getNowPlayingMovies(), true
    }

    public List<Movie> discoverMovies(int page) throws Exception {
        return processMovieResults(tmdbClient.discoverMovies(page), true
    }


    /**
     * Obtiene películas descubiertas con filtros (año, género, ordenación, rating mínimo, votos mínimos).
     *
     * @param page número de página
     * @param year año de lanzamiento (opcional)
     * @param genre ID de género (opcional)
     * @param sort criterio de ordenación (p.ej. "popularity.desc")
     * @param minRating puntuación mínima (opcional)
     * @param minVotes número mínimo de votos (opcional)
     * @return lista de películas filtradas
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
    public List<Movie> discoverMoviesFiltered(int page, Integer year, Integer genre, String sort, Double minRating, Integer minVotes) throws Exception {

        String json = tmdbClient.discoverMoviesFiltered(page, year, genre, sort, minRating, minVotes

        return processMovieResults(json, true).stream()
                .filter(m -> m.getPosterPath() != null && !m.getPosterPath().isBlank())
                .toList(
    }

    public List<TvSeries> discoverSeriesFiltered(
            int page,
            Integer year,
            Integer genre,
            String sort,
            Double minRating,
            Integer minVotes
    ) throws Exception {

        String json = tmdbClient.discoverSeriesFiltered(page, year, genre, sort, minRating, minVotes

        return processTvResults(json).stream()
                .filter(s -> s.getPosterPath() != null && !s.getPosterPath().isBlank())
                .toList(
    }

    public List<TvSeries> discoverSeriesWithPoster(int page) throws Exception {
        return processTvResults(tmdbClient.getTrendingNowTv()).stream()
                .filter(s -> s.getPosterPath() != null && !s.getPosterPath().isBlank())
                .toList(
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

    // Filtrar películas sin poster para mejorar la experiencia visual en la página de exploración
    public List<Movie> discoverMoviesWithPoster(int page) throws Exception {
        return discoverMovies(page).stream()
                .filter(m -> m.getPosterPath() != null && !m.getPosterPath().isBlank())
                .toList(
    }


    /**
     * Obtiene el mapa de IDs a nombres de géneros de películas.
     *
     * @return mapa de ID → nombre de género
     */
    public Map<Integer, String> getMovieGenresMap() {
        return movieGenreMap;
    }


    /**
     * Obtiene el mapa de IDs a nombres de géneros de series.
     *
     * @return mapa de ID → nombre de género
     */
    public Map<Integer, String> getTvGenresMap() {
        return tvGenreMap;
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


    /**
     * Obtiene contenido aleatorio (películas y series) para ver relajadamente.
     *
     * @return lista de hasta 20 medios mezclados y aleatorizados
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
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

    /**
     * Obtiene una película destacada para mostrar en el hero/portada principal.
     *
     * Selecciona películas con filtros de calidad (>300 votos, rating >6.5) y rota
     * cada hora para variedad.
     *
     * @return película destacada, o null si no hay disponible
     */
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

    /**
     * Crea un objeto Movie a partir de un nodo JSON de TMDB, adaptando los campos según el tipo de medio.
     *
     * @param node
     * @param id
     * @param isMovie
     * @return
     */
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
            m.setPosterPath(posterBaseUrl + node.path("poster_path").asText()
        }
        if (!node.path("backdrop_path").isNull()) {
            m.setBackdropPath(backdropBaseUrl + node.path("backdrop_path").asText()
        }
        m.setVoteAverage(node.path("vote_average").asDouble()
        m.setVoteCount(node.path("vote_count").asInt()
        m.setGenres(extractGenres(node, isMovie)

        return m;
    }

    /**
     * Obtiene los detalles completos de una película (sinopsis, reparto, equipo técnico, etc.).
     *
     * @param movieId identificador de la película en TMDB
     * @return objeto Movie con información completa
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
    public Movie getMovieDetails(int movieId) throws Exception {

        JsonNode json = mapper.readTree(tmdbClient.getMovieDetails(movieId)

        Movie m = new Movie(
        m.setId(json.path("id").asInt()
        m.setTitle(json.path("title").asText()
        m.setOverview(json.path("overview").asText()

        if (!json.path("poster_path").isNull()) {
            m.setPosterPath(posterBaseUrl + json.path("poster_path").asText()
        }

        if (!json.path("backdrop_path").isNull()) {
            String bestBackdrop = getBestBackdrop(movieId

            if (bestBackdrop != null) {
                m.setBackdropPath(bestBackdrop
            } else {
                m.setBackdropPath(backdropBaseUrl + json.path("backdrop_path").asText()
            }
        }

        m.setVoteAverage(json.path("vote_average").asDouble()
        m.setVoteCount(json.path("vote_count").asInt()
        m.setReleaseDate(json.path("release_date").asText()

        // runtime: Tiempo de duración de la película en minutos.
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
                return backdropBaseUrl + bestBackdrop.path("file_path").asText(
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene películas recomendadas o similares a una película dada.
     *
     * @param movieId identificador de la película de referencia
     * @return lista de hasta 20 películas relacionadas
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
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
            tv.setPosterPath(posterBaseUrl + node.path("poster_path").asText()
        }

        tv.setVoteAverage(node.path("vote_average").asDouble()
        tv.setVoteCount(node.path("vote_count").asInt()
        tv.setGenres(extractGenres(node, false)

        // fallback para series sin poster pero con backdrop, así no salen vacías en el listado
        if (!node.path("backdrop_path").isNull()) {
            tv.setBackdropPath(backdropBaseUrl + node.path("backdrop_path").asText()
        }

        return tv;
    }

    /**
     * Obtiene plataformas de streaming disponibles para una película en España.
     *
     * @param movieId identificador de la película
     * @return lista de proveedores (Netflix, Prime Video, etc.)
     */
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

    /**
     * Obtiene la URL oficial del proveedor de streaming según su ID.
     *
     * Enlaza manualmente cada ID de proveedor con su sitio web oficial, ya que TMDB
     * solo proporciona el ID y nombre sin URLs directas. Se ha incluido los proveedores
     * más comunes en España (Netflix, Prime Video, Disney+, Movistar+, HBO... etc.).
     *
     * @param id identificador del proveedor asignado por TMDB
     * @return URL del sitio web del proveedor, o null si el ID no es reconocido
     */
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

    /**
     * Obtiene reseñas de TMDB sobre una película.
     *
     * @param movieId identificador de la película
     * @return lista de reseñas mapeadas a DTO
     */
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


    /**
     * Obtiene el reparto principal de una película.
     *
     * @param movieId identificador de la película
     * @return lista de hasta 25 actores
     */
    public List<CastMember> getMovieCast(int movieId) {

        try {
            String json = tmdbClient.getMovieCredits(movieId

            JsonNode root = mapper.readTree(json
            JsonNode castArray = root.path("cast"

            List<CastMember> castList = new ArrayList<>(

            int limit = Math.min(castArray.size(), 25

            for (int i = 0; i < limit; i++) {

                JsonNode actor = castArray.get(i

                String name = actor.path("name").asText(
                String character = actor.path("character").asText(

                String profilePath = actor.path("profile_path").asText(null

                if (profilePath != null && !profilePath.isBlank()) {
                    profilePath = "https://image.tmdb.org/t/p/w185" + profilePath;
                } else {
                    profilePath = null; // fallback controlado (Se maneja en la vista con Thymleaf)
                }

                castList.add(new CastMember(name, character, profilePath)
            }

            return castList;

        } catch (Exception e) {
            e.printStackTrace(
            return Collections.emptyList(
        }
    }


    /**
     * Obtiene información técnica de una película (director, guionistas, compositor, cinematografía).
     *
     * @param movieId identificador de la película
     * @return mapa con claves "directors", "writers", "composer", "cinematography"
     */
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


    /**
     * Obtiene los detalles completos de una serie (sinopsis, equipo técnico, número de temporadas, etc.).
     *
     * @param tvId identificador de la serie en TMDB
     * @return objeto TvSeries con información completa
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
    public TvSeries getSerieDetails(int tvId) throws Exception {

        JsonNode json = mapper.readTree(tmdbClient.getTvDetails(tvId)

        TvSeries tv = new TvSeries(

        tv.setId(json.path("id").asInt()
        tv.setTitle(json.path("name").asText()
        tv.setOverview(json.path("overview").asText()

        if (!json.path("poster_path").isNull()) {
            tv.setPosterPath(posterBaseUrl + json.path("poster_path").asText()
        }

        if (!json.path("backdrop_path").isNull()) {
            tv.setBackdropPath(backdropBaseUrl + json.path("backdrop_path").asText()
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


    /**
     * Obtiene el reparto principal de una serie.
     *
     * @param tvId identificador de la serie
     * @return lista de hasta 25 actores
     */
    public List<CastMember> getSerieCast(int tvId) {

        try {
            String json = tmdbClient.getTvCredits(tvId

            JsonNode root = mapper.readTree(json
            JsonNode castArray = root.path("cast"

            List<CastMember> castList = new ArrayList<>(

            int limit = Math.min(castArray.size(), 25

            for (int i = 0; i < limit; i++) {

                JsonNode actor = castArray.get(i
                String name = actor.path("name").asText(
                JsonNode roles = actor.path("roles"

                String character = "";
                if (roles.isArray() && roles.size() > 0) {
                    character = roles.get(0).path("character").asText(
                }

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


    /**
     * Obtiene series recomendadas o similares a una serie dada.
     *
     * @param tvId identificador de la serie de referencia
     * @return lista de hasta 20 series relacionadas
     * @throws Exception si ocurre error en la comunicación con TMDB
     */
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


    /**
     * Obtiene plataformas de streaming disponibles para una serie en España.
     *
     * @param tvId identificador de la serie
     * @return lista de proveedores
     */
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


    /**
     * Obtiene información técnica de una serie (creadores).
     *
     * @param tvId identificador de la serie
     * @return mapa con claves "directors", "writers", "composer", "cinematography"
     */
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


    /**
     * Obtiene reseñas de TMDB sobre una serie.
     *
     * @param tvId identificador de la serie
     * @return lista de reseñas mapeadas a DTO
     */
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

    /**
     * Obtiene la clave del trailer (YouTube) de una película o serie desde TMDB.
     *
     * Busca el primer video de tipo "Trailer" en español o inglés alojado en YouTube.
     * Si no encuentra un trailer exacto, intenta como alternativa cualquier video que
     * contenga la palabra "trailer" en su nombre. Retorna null si no hay videos disponibles.
     *
     * @param id identificador de la película o serie en TMDB
     * @param isMovie true si es película, false si es serie
     * @return la clave del video (key) de YouTube del trailer, o null si no hay disponible
     */
    private String getTrailerKey(int id, boolean isMovie) {

        try {
            // Validar si es película o serie y llamar al endpoint correspondiente para obtener los videos asociados
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

                // PRIORIDAD 2 (fallback) → algún clip que tenga "pinta" de trailer
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


    // Añadir proveedores de cualquier categoría evitando duplicados (a veces un mismo proveedor aparece en varias categorías, como flatrate y buy)
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