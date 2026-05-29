package com.mara.tfgcine.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO que representa una reseña de película o serie desde la API de TMDB.
 *
 * Esta clase se utiliza para parsear las reseñas que devuelve la API de TMDB
 * y convertir el JSON de respuesta a objetos Java. No se persiste en la base de datos;
 * es un modelo de transporte de datos para consultar reseñas externas de TMDB.
 *
 * Tiene una clase anidada AuthorDetails porque representa la estructura JSON
 * anidada que devuelve la API de TMDB
 *
 * Campos principales:
 * - author: nombre del usuario que escribió la reseña en TMDB
 * - content: texto o comentario de la reseña
 * - createdAt: fecha de creación en formato string (de TMDB)
 * - authorDetails: objeto anidado con información adicional del autor (rating, avatar)
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see TmdbReview.AuthorDetails
 */
@Data
public class TmdbReview {

    private String author;
    private String content;

    @JsonProperty("created_at")
    private String createdAt; // string para evitar problemas de parsing

    @JsonProperty("author_details")
    private AuthorDetails authorDetails;

    /**
     * Clase anidada que representa los detalles del autor de una reseña en TMDB.
     *
     * Contiene datos del usuario que escribió la reseña en TMDB.
     *
     * Campos:
     * - rating: calificación que el autor asignó (puede ser nula)
     * - avatarPath: ruta al avatar del usuario en TMDB (puede ser nula)
     */
    @Data
    public static class AuthorDetails {
        private Double rating;

        @JsonProperty("avatar_path")
        private String avatarPath;

     /*   public Double getRating() { return rating; }
        public void setRating(Double rating) { this.rating = rating; }

        public String getAvatarPath() { return avatarPath; }
        public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }*/
    }

/*    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public AuthorDetails getAuthorDetails() { return authorDetails; }
    public void setAuthorDetails(AuthorDetails authorDetails) { this.authorDetails = authorDetails; }*/
}