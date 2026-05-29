package com.mara.tfgcine.model.moderation;

/**
 * Enumeración que define los tipos de acciones de moderación disponibles.
 *
 * Cada constante representa una acción específica que un moderador puede realizar
 * en la aplicación para controlar y gestionar el contenido y el comportamiento de usuarios.
 *
 * Constantes:
 * - DELETE_REVIEW: eliminar una reseña del sistema
 * - BAN_USER: banear permanentemente a un usuario de la plataforma
 * - DISABLE_ACCOUNT: deshabilitar temporalmente la cuenta de un usuario
 * - REMOVE_MODERATOR_ROLE: remover el rol de moderador a un usuario
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see ModerationAction
 */

public enum ModerationActionType {
    DELETE_REVIEW,
    BAN_USER,
    DISABLE_ACCOUNT,
    REMOVE_MODERATOR_ROLE
}