package com.mara.tfgcine.model.dto;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.status.MediaStatus;


/**
 * DTO de petición utilizado para enviar el estado de visualización de un contenido multimedia.
 *
 * Contiene el identificador del contenido, su tipo, el estado seleccionado y un pequeño
 * snapshot de datos TMDB para poder persistir o actualizar la información asociada al usuario.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */

public class StatusRequestDTO {

    private Long mediaId;

    private MediaType mediaType;

    private MediaStatus status;

    /**
     * SNAPSHOT TMDB:
     * Datos auxiliares de TMDB necesarios para persistir el contenido multimedia.
     *
     * Se incluyen solo como información de apoyo para guardar el título, el póster
     * y la valoración media asociados al medio seleccionado.
     */

    private String title;
    private String posterPath;
    private Double voteAverage;

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

}