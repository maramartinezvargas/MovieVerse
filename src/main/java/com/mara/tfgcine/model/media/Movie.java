package com.mara.tfgcine.model.media;
import lombok.Data;
@Data
public class Movie extends Media {

    private String backdropPath;

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

}