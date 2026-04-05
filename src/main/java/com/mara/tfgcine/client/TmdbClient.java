package com.mara.tfgcine.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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


    public TmdbClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchMulti(String query) {

        String url = baseUrl + "/search/multi"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&query=" + query
                + "&include_adult=false"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    public String getTrendingMovies() {
        String url = baseUrl + "/trending/movie/day"
                + "?api_key=" + apiKey
                + "&region=ES"
                + "&language=" + language;
        return restTemplate.getForObject(url, String.class
    }

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

    public String getPopularTv() {
        String url = baseUrl + "/tv/popular"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&page=1";
        return restTemplate.getForObject(url, String.class
    }

    public String getTrendingTv() {
        String url = baseUrl + "/trending/tv/day"
                + "?api_key=" + apiKey
                + "&region=ES"
                + "&language=" + language
                + "&page=1";
        return restTemplate.getForObject(url, String.class
    }

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

    public String getOnTheAirTv() {

        String url = baseUrl + "/tv/on_the_air"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    public String getMovieGenres() {
        String url = baseUrl + "/genre/movie/list?api_key=" + apiKey + "&language=" + language;
        return restTemplate.getForObject(url, String.class
    }

    public String getTvGenres() {
        String url = baseUrl + "/genre/tv/list?api_key=" + apiKey + "&language=" + language;
        return restTemplate.getForObject(url, String.class
    }

    public String getMovieDetails(int movieId) {

        String url = baseUrl +
                "/movie/" + movieId +
                "?api_key=" + apiKey +
                "&language=" + language;

        return restTemplate.getForObject(url, String.class
    }

    public String getNowPlayingMovies() {

        String url = baseUrl + "/movie/now_playing"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&region=ES"
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    public String getTvDetailsEnglish(int tvId) {

        String url = baseUrl +
                "/tv/" + tvId +
                "?api_key=" + apiKey +
                "&language=" + fallbackLanguage;

        return restTemplate.getForObject(url, String.class
    }

    public String getBestMoviesThisYear() {

        // AÑO PASADO
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

    public String discoverMovies() {

        String url = baseUrl + "/discover/movie"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&sort_by=vote_average.desc"
                + "&vote_count.gte=2000"
                + "&region=ES"
                + "&page=2";


        return restTemplate.getForObject(url, String.class
    }

    public String getMovieImages(int movieId) {
        return restTemplate.getForObject(
                baseUrl + "/movie/" + movieId + "/images?api_key=" + apiKey,
                String.class
        
    }

    public String getMovieRecommendations(int movieId) {
        String url = baseUrl + "/movie/" + movieId + "/recommendations"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

    public String getMovieSimilar(int movieId) {
        String url = baseUrl + "/movie/" + movieId + "/similar"
                + "?api_key=" + apiKey
                + "&language=" + language
                + "&page=1";

        return restTemplate.getForObject(url, String.class
    }

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

}