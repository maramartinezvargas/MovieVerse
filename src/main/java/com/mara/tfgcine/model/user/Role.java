package com.mara.tfgcine.model.user;

/**
 * Enumeración que define los roles de usuario disponibles en la aplicación.
 *
 * Cada constante representa un nivel de permisos y responsabilidades dentro de la plataforma.
 *
 * Constantes:
 * - ADMIN: administrador con permisos completos en la aplicación (gestión de usuarios, moderadores, etc.)
 * - MODERATOR: moderador con permisos para revisar y resolver reportes de contenido
 * - STANDARD: usuario estándar con permisos básicos (crear reseñas, dar likes, crear listas, etc.)
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.user.User
 */

public enum Role {
    ADMIN,
    MODERATOR,
    STANDARD
}
