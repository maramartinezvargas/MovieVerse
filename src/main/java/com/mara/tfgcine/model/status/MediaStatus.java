package com.mara.tfgcine.model.status;

/**
 * Enumeración que define los estados de visualización de una película o serie en el perfil del usuario.
 *
 * Cada constante indica si el usuario ha visto o planea ver un contenido multimedia.
 *
 * Constantes:
 * - WATCHED: el usuario ya ha visto la película o serie
 * - WATCHLIST: el usuario planea ver la película o serie en el futuro
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.status.UserMediaStatus
 */
public enum MediaStatus {
    WATCHED,
    WATCHLIST
}