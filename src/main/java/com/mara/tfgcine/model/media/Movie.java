package com.mara.tfgcine.model.media;
import lombok.Data;

import java.util.List;

@Data
public class Movie extends Media {

    private String backdropPath;
    private Integer runtime; // Duración en minutos
    private String trailerKey;
    private List<String> productionCompanies;
    private List<String> productionCountries;
    private String originalLanguage;

    // Formato fecha (usa la de Media)
    public String getFormattedReleaseDate() {

        String date = getReleaseDate(

        if (date == null || date.isBlank()) {
            return "Fecha desconocida";
        }

        try {
            String[] parts = date.split("-"
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            } else {
                return date;
            }
        } catch (Exception e) {
            return date;
        }
    }

    // Formato duración en horas y minutos
    public String getFormattedRuntime() {
        if (runtime == null || runtime <= 0) return "";
        int h = runtime / 60;
        int m = runtime % 60;

        if (h > 0) {
            return h + "h " + m + "min";
        }
        return m + "min";
    }



}