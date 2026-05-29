package com.mara.tfgcine.model.dto;

import java.time.LocalDateTime;


/**
 * DTO de respuesta utilizado para exponer los datos de una reseña.
 *
 * Contiene la información necesaria para mostrar o editar una reseña en la interfaz,
 * incluyendo identificador, puntuación, comentario, fecha de creación, contenido asociado,
 * tipo de medio y nombre de usuario.
 *
 * @author Tamara Martínez Vargas
 * @since 02/06/2026
 * @version 28/05/2026
 */
public class ReviewResponseDTO {

    private Long id;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    private Long mediaId;

    private String mediaType;

    private String username;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(Long id,
                             Integer rating,
                             String comment,
                             LocalDateTime createdAt,
                             Long mediaId,
                             String mediaType,
                             String username) {

        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.username = username;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getUsername() {
        return username;
    }
}