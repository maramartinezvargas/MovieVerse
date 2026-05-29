package com.mara.tfgcine.model.media;

import lombok.Data;

import java.util.List;

/**
 * Modelo que representa una serie de televisión obtenida desde TMDB.
 *
 * Contiene los datos específicos de una serie (fecha de primer episodio,
 * temporadas/episodios, información de producción, duración de episodio, etc.)
 * y extiende {@link Media} para compartir campos
 * comunes (título, póster, sinopsis, puntuación, etc.).
 *
 * Esta clase se utiliza por el servicio {@link com.mara.tfgcine.service.TmdbService}
 * para mapear respuestas de la API de TMDB a objetos de la aplicación y para
 * mostrar/transferir información en las vistas (no es una entidad JPA).
 *
 * Notas:
 * - Algunos getters/ setters sobrescriben o complementan la funcionalidad de {@code Media}.
 * - Campos relevantes: {@code firstAirDate}, {@code numberOfSeasons}, {@code numberOfEpisodes},
 *   {@code episodeRuntime}, {@code backdropPath}, {@code trailerKey}, {@code productionCompanies}, etc.
 *
 * @author Tamara Martínez
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.media.Media
 * @see com.mara.tfgcine.service.TmdbService
 */
@Data
public class TvSeries extends Media {

    private String firstAirDate;
    private int seasonNumber;
    private int numberOfSeasons;
    private int numberOfEpisodes;

    private String overview;
    private String backdropPath;
    private String trailerKey;

    private List<String> productionCompanies;
    private List<String> productionCountries;
    private String originalLanguage;
    private String status;
    private Integer episodeRuntime;

    // GETTERS & SETTERS
   /* public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {this.backdropPath = backdropPath;}

    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public List<String> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<String> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<String> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<String> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEpisodeRuntime() {
        return episodeRuntime;
    }

    public void setEpisodeRuntime(Integer episodeRuntime) {
        this.episodeRuntime = episodeRuntime;
    }*/
}