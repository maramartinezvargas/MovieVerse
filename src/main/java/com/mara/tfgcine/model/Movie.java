package com.mara.tfgcine.model;

import lombok.Data;

import java.util.List;

/**
 * Modelo que representa una película de TMDB
 */
@Data
public class Movie {

    private int id;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private double voteAverage;
    private List<String> genres;
    private int voteCount;
    private String releaseDate;
    private Integer seasonNumber;

    public Movie() {}

    public String getVoteAverageFormatted() {
        return String.format(java.util.Locale.US, "%.1f", voteAverage
    }



}
