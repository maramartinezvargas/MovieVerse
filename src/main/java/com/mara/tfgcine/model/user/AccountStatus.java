package com.mara.tfgcine.model.user;

/**
 * Enumeración que define los estados posibles de una cuenta de usuario.
 *
 * Cada constante indica el estado de actividad y disponibilidad de la cuenta de un usuario.
 *
 * Constantes:
 * - ACTIVE: la cuenta está activa y el usuario puede acceder normalmente a la aplicación
 * - BANNED: la cuenta ha sido baneada permanentemente por moderadores (usuario no puede acceder)
 * - DISABLED: la cuenta ha sido deshabilitada temporalmente por el usuario o moderadores
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.user.User
 */

public enum AccountStatus {
    ACTIVE,
    BANNED,
    DISABLED
}