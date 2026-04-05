package com.mara.tfgcine.model.media;

import java.util.List;

public abstract class Media {

    public int id;
    public String title;
    public String posterPath;
    public double voteAverage;

    public int voteCount;
    public List<String> genres;

    public String mediaType;

    public Integer seasonNumber;

    public List<String> getGenres() {
        return genres;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public String getVoteAverageFormatted() {
        return String.format("%.1f", voteAverage
    }

    // Obtener url según sea pelicula o serie
    public String getUrlPath() {
        if (this instanceof Movie) {
            return "/peliculas/" + id;
        } else {
            return "/series/" + id;
        }
    }
}