package com.mara.tfgcine.model.media;


/**
 * ACTUALMENTE NO SE UTILIZA EN LA APLICACIÓN, PERO SE RESERVA PARA FUTURAS MEJORAS
 * DTO que representa un género de contenido multimedia.
 *
 * Contiene el identificador del género y su nombre, normalmente obtenido desde TMDB
 * para mostrar filtros o etiquetas en la interfaz.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
public class CastMember {

    private String name;
    private String character;
    private String profilePath;

    public CastMember() {}

    public CastMember(String name, String character, String profilePath) {
        this.name = name;
        this.character = character;
        this.profilePath = profilePath;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getProfilePath() {
        return profilePath;
    }
}