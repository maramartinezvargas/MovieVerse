package com.mara.tfgcine.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.mara.tfgcine.model.review.TmdbReview;

/**
 * Cliente HTTP para interactuar con la API de The Movie Database (TMDB).
 *
 * Proporciona métodos para obtener información sobre películas, series de televisión,
 * tendencias, recomendaciones, reseñas y proveedores de visualización desde TMDB.
 *
 * Esta clase utiliza RestTemplate de Spring para realizar peticiones HTTP
 * a los endpoints de la API de TMDB. Las claves API y configuración se inyectan desde
 * las propiedades de la aplicación (application.properties).
 *
 * @author Tamara Martínez Vargas
 * @since 28/05/2026
 * @version 28/05/2026
 */

@Component
public class TmdbClient {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base}")
    private String baseUrl;

    @Value("${tmdb.api.language.primary}")
    private String language;

    @Value("${tmdb.api.language.fallback}")
    private String fallbackLanguage;


    /**
     * Constructor que inyecta la dependencia de RestTemplate.
     *
     * @param restTemplate el cliente REST de Spring para realizar peticiones HTTP
     */
    public TmdbClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Búsqueda y Géneros ------------------------------------------------------------------------

    /**
     * Busca películas y series según una consulta de búsqueda.
     *
     * @param query la cadena de búsqueda
     * @return una cadena JSON con los resultados de la búsqueda multi (películas y series)
     */
    public String searchMulti(String query) {

        String url = baseUrl + "/search/multi"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&query=" + query
                + "&include_adult=false"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene la lista de géneros disponibles para películas.
     *
     * @return una cadena JSON con los géneros de películas
     */
    public String getMovieGenres() {
        String url = baseUrl + "/genre/movie/list?api_key=" + apiKey + "&language=" + language;
        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene la lista de géneros disponibles para series de televisión.
     *
     * @return una cadena JSON con los géneros de series
     */
    public String getTvGenres() {
        String url = baseUrl + "/genre/tv/list?api_key=" + apiKey + "&language=" + language;
        return restTemplate.getForObject(url, String.class
    }


    // MÉTODOS DE PELÍCULAS --------------------------------------------------------------------------------------------

    // Tendencias y descubrimiento de películas
    /**
     * Obtiene las películas tendencia del día.
     *
     * @return una cadena JSON con las películas tendencia del día en la región ES
     */
    public String getTrendingMovies() {
        String url = baseUrl + "/trending/movie/day"
                + "?api_key=" + apiKey
                + "&region=ES"
                + "&language=" + language;
        return restTemplate.getForObject(url, String.class
    }


    /**
     * Obtiene películas populares de los últimos 2 años con al menos 200 votos.
     * Filtra películas con al menos 200 votos y ordena por popularidad descendente.
     *
     * @return una cadena JSON con las películas tendencia actuales
     */

    public String getTrendingNowMovies() {

        int year = java.time.Year.now().getValue() - 2;

        String url = baseUrl + "/discover/movie"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&sort_by=popularity.desc"
                + "&vote_count.gte=200"
                + "&primary_release_date.gte=" + year + "-01-01"
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene películas actualmente en cartelera.
     *
     * @return una cadena JSON con las películas ahora en cines en la región ES
     */
    public String getNowPlayingMovies() {

        String url = baseUrl + "/movie/now_playing"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene las películas mejor calificadas del año anterior.
     *
     * Filtra películas con al menos 2500 votos y las ordena por puntuación descendente.
     *
     * @return una cadena JSON con las mejores películas del año pasado en la región ES
     */
    public String getBestMoviesThisYear() {

        // Obtener el valor del año anterior al actual (por ejemplo, si estamos en 2026, se usará 2025)
        int year = java.time.Year.now().getValue() - 1;

        String url = baseUrl + "/discover/movie"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&sort_by=vote_average.desc"
                + "&vote_count.gte=2500"
                + "&primary_release_year=" + year
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene películas clásicas de confort (bien calificadas y "antigüas" - anteriores a 2011).
     *
     * Selecciona películas con puntuación de al menos 8.0, más de 3000 votos
     * y lanzadas antes del 2021. Útil para una categoría de "películas nostálgicas".
     *
     * @return una cadena JSON con películas de confort
     */
    public String getComfortMovies() {

        String url = baseUrl + "/discover/movie"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&sort_by=popularity.desc"
                + "&vote_average.gte=8"
                + "&vote_count.gte=3000"
                + "&primary_release_date.lte=2020-12-31"
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Descubre películas con paginación.
     *
     * Ordena por popularidad descendente sin aplicar filtros adicionales.
     *
     * @param page el número de página a recuperar (comienza en 1)
     * @return una cadena JSON con películas descubiertas
     */
    public String discoverMovies(int page) {

        String url = baseUrl + "/discover/movie"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&sort_by=popularity.desc"
                + "&region=ES"
                + "&page=" + page;

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Descubre películas con filtros avanzados.
     *
     * Permite filtrar películas por año de lanzamiento, género, ordenamiento,
     * puntuación mínima y cantidad mínima de votos. Los filtros de votos y puntuación
     * se ajustan automáticamente según el criterio de ordenamiento seleccionado.
     *
     * @param page el número de página a recuperar (comienza en 1)
     * @param year el año de lanzamiento (opcional, puede ser null)
     * @param genre el ID del género (opcional, puede ser null)
     * @param sort el criterio de ordenamiento: "vote_average.desc", "primary_release_date.desc",
     *             "popularity.desc" (opcional, puede ser null)
     * @param minRating la puntuación mínima (opcional, puede ser null)
     * @param minVotes la cantidad mínima de votos (opcional, puede ser null)
     * @return una cadena JSON con las películas filtradas
     */
    public String discoverMoviesFiltered(int page, Integer year, Integer genre, String sort, Double minRating, Integer minVotes) {

        String today = java.time.LocalDate.now().toString(

        StringBuilder url = new StringBuilder(baseUrl + "/discover/movie")
                .append("?api_key=").append(apiKey)
                .append("&language=").append(language)
                .append("&region=ES")
                .append("&page=").append(page)
                .append("&primary_release_date.lte=").append(today

        // Filtros por defecto si no se proporcionan valores específicos
        if (year != null) {
            url.append("&primary_release_year=").append(year
        }

        if (genre != null) {
            url.append("&with_genres=").append(genre
        }

        double rating = (minRating != null) ? minRating : 0;
        int votes = (minVotes != null) ? minVotes : 0;

        int finalVotes = votes;
        double finalRating = rating;

        if (sort != null && !sort.isBlank()) {

            url.append("&sort_by=").append(sort

            switch (sort) {

                case "vote_average.desc" -> {
                    finalVotes = Math.max(votes, 2000
                    finalRating = Math.max(rating, 6.5
                }

                case "primary_release_date.desc" -> {
                    finalVotes = Math.max(votes, 50
                }

                case "popularity.desc" -> {
                    finalVotes = Math.max(votes, 100
                }
            }
        }

        url.append("&vote_average.gte=").append(finalRating
        url.append("&vote_count.gte=").append(finalVotes

        return restTemplate.getForObject(url.toString(), String.class
    }

    // Detalles y Metadata de películas

    /**
     * Obtiene los detalles completos de una película.
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con la información detallada de la película
     */
    public String getMovieDetails(int movieId) {

        String url = baseUrl +
                "/movie/" + movieId +
                "?api_key=" + apiKey +
                "&language=" + language;

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene el reparto y equipo de producción de una película.
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con los créditos (actores, directores, productores, etc.)
     */
    public String getMovieCredits(int movieId) {

        String url = baseUrl +
                "/movie/" + movieId + "/credits" +
                "?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene las imágenes asociadas a una película.
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con las imágenes (posters, fondos, etc.)
     */

    public String getMovieImages(int movieId) {
        return restTemplate.getForObject(
                baseUrl + "/movie/" + movieId + "/images?api_key=" + apiKey,
                String.class
        
    }


    /**
     * Obtiene los vídeos (trailers) asociados a una película.
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con los vídeos disponibles
     */
    public String getMovieVideos(int movieId) {

        String url = baseUrl + "/movie/" + movieId + "/videos"
                + "?api_key=" + apiKey
                + "&language=" + language;

        return restTemplate.getForObject(url, String.class
    }

    // Relaciones de películas (Títulos recomendados, relacionados... a partir de un título)

    /**
     * Obtiene películas recomendadas basadas en una película específica.
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con las películas recomendadas
     */
    public String getMovieRecommendations(int movieId) {
        String url = baseUrl + "/movie/" + movieId + "/recommendations"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene películas similares a una película específica.
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con las películas similares
     */
    public String getMovieSimilar(int movieId) {
        String url = baseUrl + "/movie/" + movieId + "/similar"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    // Información adicional de películas

    /**
     * Obtiene los proveedores de visualización disponibles para una película.
     *
     * Proporciona información sobre dónde ver la película (proveedores/plataformas de streaming).
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con los proveedores de visualización
     */
    public String getWatchProviders(int movieId) {
        String url = baseUrl +
                "/movie/" + movieId +
                "/watch/providers?api_key=" + apiKey;
        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene las reseñas de usuarios (de TMDB) para una película.
     *
     * @param movieId el identificador único de la película en TMDB
     * @return una cadena JSON con las reseñas disponibles
     */
    public String getMovieReviews(int movieId) {

        String url = baseUrl +
                "/movie/" + movieId + "/reviews" +
                "?api_key=" + apiKey;
        //+ "&language=es-ES";

        return restTemplate.getForObject(url, String.class
    }


    // MÉTODOS DE SERIES --------------------------------------------------------------------------------------------

    // Tendencias y descubrimiento de series
    /**
     * Obtiene las series tendencia del día.
     *
     * @return una cadena JSON con las series tendencia del día en la región ES
     */
    public String getTrendingTv() {
        String url = baseUrl + "/trending/tv/day"
                + "?api_key=" + apiKey
                + "&region=ES"
                + "&language=" + language
                + "&page=1";
        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene series populares del último año.
     *
     * <p>Filtra series con al menos 100 votos y ordena por popularidad descendente.</p>
     *
     * @return una cadena JSON con las series tendencia actuales
     */
    public String getTrendingNowTv() {

        int year = java.time.Year.now().getValue() - 1;

        String url = baseUrl + "/discover/tv"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&sort_by=popularity.desc"
                + "&vote_count.gte=100"
                + "&first_air_date.gte=" + year + "-01-01"
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }


    /**
     * Obtiene series que actualmente se están emitiendo.
     *
     * @return una cadena JSON con las series actualmente en emisión en la región ES
     */
    public String getOnTheAirTv() {

        String url = baseUrl + "/tv/on_the_air"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene series de televisión populares.
     *
     * @return una cadena JSON con las series populares
     */
    public String getPopularTv() {
        String url = baseUrl + "/tv/popular"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&page=1";
        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene series clásicas de confort (bien calificadas y "antigüas" - anteriores a 2011).
     *
     * Selecciona series con puntuación de al menos 7.0, más de 1000 votos
     * y estrenadas antes del 2021. Útil para una categoría de "series nostálgicas".
     *
     * @return una cadena JSON con series de confort
     */
    public String getComfortTv() {

        String url = baseUrl + "/discover/tv"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&sort_by=popularity.desc"
                + "&vote_average.gte=7"
                + "&vote_count.gte=1000"
                + "&first_air_date.lte=2020-12-31"
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Descubre series con filtros avanzados.
     *
     * Permite filtrar series por año de estreno, género, ordenamiento,
     * puntuación mínima y cantidad mínima de votos.
     *
     * @param page el número de página a recuperar (comienza en 1)
     * @param year el año de estreno (opcional, puede ser null)
     * @param genre el ID del género (opcional, puede ser null)
     * @param sort el criterio de ordenamiento (opcional, puede ser null)
     * @param minRating la puntuación mínima (opcional, puede ser null)
     * @param minVotes la cantidad mínima de votos (opcional, puede ser null)
     * @return una cadena JSON con las series filtradas
     */
    public String discoverSeriesFiltered(int page, Integer year, Integer genre, String sort, Double minRating, Integer minVotes) {

        String today = java.time.LocalDate.now().toString(

        StringBuilder url = new StringBuilder(baseUrl + "/discover/tv")
                .append("?api_key=").append(apiKey)
                .append("&language=").append(language)
                .append("&region=ES")
                .append("&page=").append(page)
                .append("&first_air_date.lte=").append(today

        if (year != null) {
            url.append("&first_air_date_year=").append(year
        }

        if (genre != null) {
            url.append("&with_genres=").append(genre
        }

        double rating = (minRating != null) ? minRating : 0;
        int votes = (minVotes != null) ? minVotes : 0;

        if (sort != null && !sort.isBlank()) {
            url.append("&sort_by=").append(sort
        }

        url.append("&vote_average.gte=").append(rating
        url.append("&vote_count.gte=").append(votes

        return restTemplate.getForObject(url.toString(), String.class
    }

    // Detalles y Metadata de series

    /**
     * Obtiene los detalles completos de una serie de televisión.
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con la información detallada de la serie
     */
    public String getTvDetails(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId +
                "?api_key=" + apiKey +
                "&language=" + language;

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene los detalles de una serie de televisión en inglés.
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con la información detallada de la serie en idioma de fallback
     */
    public String getTvDetailsEnglish(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId +
                "?api_key=" + apiKey +
                "&language=" + fallbackLanguage;

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene el reparto agregado de una serie de televisión.
     *
     * Proporciona información consolidada del elenco a lo largo de todas las temporadas.
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con los créditos agregados de la serie
     */
    public String getTvCredits(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId + "/aggregate_credits" +
                "?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene los vídeos (trailers, clips) asociados a una serie.
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con los vídeos disponibles
     */
    public String getTvVideos(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId + "/videos" +
                "?api_key=" + apiKey +
                "&language=" + language;

        return restTemplate.getForObject(url, String.class
    }

    // Relaciones de series: recomendaciones, similares, proveedores de visualización, reseñas...

    /**
     * Obtiene series recomendadas basadas en una serie específica.
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con las series recomendadas
     */
    public String getTvRecommendations(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId + "/recommendations" +
                "?api_key=" + apiKey +
                "&language=" + language +
                "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene series similares a una serie específica.
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con las series similares
     */
    public String getTvSimilar(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId + "/similar" +
                "?api_key=" + apiKey +
                "&language=" + language +
                "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    // Información adicional de series

    /**
     * Obtiene los proveedores de visualización disponibles para una serie.
     *
     *  Proporciona información sobre dónde ver la serie (proveedores/plataformas de streaming).
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con los proveedores de visualización
     */
        public String getTvWatchProviders(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId +
                "/watch/providers?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class
    }

    /**
     * Obtiene las reseñas de usuarios para una serie.
     *
     * @param tvId el identificador único de la serie en TMDB
     * @return una cadena JSON con las reseñas disponibles
     */
    public String getTvReviews(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId + "/reviews" +
                "?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class
    }
}