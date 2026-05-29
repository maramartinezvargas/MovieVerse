package com.mara.tfgcine.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * DTO utilizado para representar una reseña mostrada en el perfil o en las fichas de contenido.
 *
 * <p>Contiene la información necesaria para mostrar reseñas de usuarios o de TMDB,
 * incluyendo identificador, nombre de usuario, comentario, puntuación, fecha de creación,
 * origen de la reseña y avatar del usuario.</p>
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
public class ReviewDTO {

    private Long id;
    private String username;
    private String comment;
    private Double rating;
    private LocalDateTime createdAt;
    private String source; // "LOCAL" o "TMDB"
    private String avatarUrl; // Imagen Avatar TMDB user

    // getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    /**
     * Compara dos instancias de ReviewDTO por su comentario
     *
     * Se utiliza este criterio para considerar iguales las reseñas con el mismo contenido textual.
     *
     * @param o objeto a comparar
     * @return @code true si ambos objetos tienen el mismo comentario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewDTO)) return false;
        ReviewDTO that = (ReviewDTO) o;
        return Objects.equals(comment, that.comment
    }

    /**
     * Calcula el hash del DTO a partir del comentario.
     *
     * @return hash basado en el comentario
     */
    @Override
    public int hashCode() {
        return Objects.hash(comment
    }
}