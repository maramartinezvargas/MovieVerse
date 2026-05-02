package com.mara.tfgcine.model.media;

import java.util.List;

public abstract class Media {

    private int id;
    private String title;
    private String posterPath;
    private double voteAverage;
    private int voteCount;
    private List<String> genres;
    private String mediaType;
    private String overview;
    private String releaseDate;
    protected String backdropPath;

    // Getters ----------------------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }


    // Setters ------------------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    // Otros métodos ------------------------------------------------------------------------------

    public String getVoteAverageFormatted() {
        return String.format("%.1f", voteAverage
    }

    public String getUrlPath() {
        if ("movie".equals(mediaType)) {
            return "/peliculas/" + id;
        } else {
            return "/series/" + id;
        }
    }

    public String getYear() {
        if (releaseDate == null || releaseDate.isBlank()) return "";
        try {
            return releaseDate.substring(0, 4
        } catch (Exception e) {
            return "";
        }
    }

    public int getStars() {
        return Math.min(10, (int) Math.round(this.voteAverage)
    }
}