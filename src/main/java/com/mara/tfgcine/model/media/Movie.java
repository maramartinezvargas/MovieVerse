package com.mara.tfgcine.model.media;

import lombok.Data;

@Data
public class Movie extends Media {

    public String releaseDate;
    public String overview;
    public String backdropPath;

    // actualizar formato fecha a dd/MM/yyyy
    public String getFormattedReleaseDate() {
        if (releaseDate == null || releaseDate.isBlank()) {
            return "Fecha desconocida";
        }
        try {
            String[] parts = releaseDate.split("-"
            if (parts.length == 3) {
                return parts[2] + " / " + parts[1] + " / " + parts[0];
            } else {
                return releaseDate; // formato inesperado, devolver tal cual
            }
        } catch (Exception e) {
            return releaseDate; // en caso de error, devolver tal cual
        }
    }
}