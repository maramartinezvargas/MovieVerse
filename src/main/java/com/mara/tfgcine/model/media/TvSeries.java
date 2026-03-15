package com.mara.tfgcine.model.media;

import jakarta.persistence.Table;

public class TvSeries extends Media {

    private String firstAirDate;
    private int seasonNumber;
    private String overview;
    private String backdropPath;

}