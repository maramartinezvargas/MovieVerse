package com.mara.tfgcine.model.moderation;

/**
 * Enumeración que define los estados posibles de un reporte de reseña.
 *
 * Representa el ciclo de vida de un reporte desde su creación hasta su resolución
 * por parte de un moderador.
 *
 * Constantes:
 * - PENDING: reporte recién creado, pendiente de revisión por un moderador
 * - UNDER_REVIEW: reporte actualmente siendo revisado por un moderador
 * - RESOLVED: reporte resuelto, la reseña reportada ha sido eliminada
 * - REJECTED: reporte rechazado, se consideró que la reseña es válida y no requiere acción
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see Report
 */

public enum ReportStatus {
    PENDING,
    UNDER_REVIEW,
    RESOLVED,
    REJECTED;

    /**
     * Devuelve una etiqueta en español para mostrar un estado del reporte legible en la interfaz de usuario.
     *
     * @return cadena con la etiqueta correspondiente al estado
     */
    public String getLabel() {

        return switch (this) {

            case PENDING -> "Pendiente";
            case UNDER_REVIEW -> "Bajo revisión";
            case RESOLVED -> "Aceptado";
            case REJECTED -> "Rechazado";
        };
    }
}