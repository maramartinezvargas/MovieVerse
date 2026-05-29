package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * NOTA: Esta funcionalidad actualmente NO SE UTILIZA desde la aplicación Java.
 * La entidad está definida y la tabla {@code moderation_actions} existe en la BD, pero no hay
 * repositorio ni código que persista registros en esta tabla.
 * ---------------------------------------------------------------------------------------------
 * Entidad JPA que registra acciones de moderación realizadas sobre usuarios y contenido.
 *
 * Cada instancia representa una acción explícita (eliminar reseña, banear usuario, deshabilitar cuenta, etc.)
 * ejecutada por un moderador y se persiste en la tabla {@code moderation_actions}.
 *
 * Campos principales:
 * - actionType: tipo de acción (enum {@link ModerationActionType})
 * - moderatorId: identificador del moderador que la ejecutó
 * - targetUserId: usuario afectado (opcional)
 * - targetReviewId: reseña afectada (opcional)
 * - createdAt: fecha y hora de la acción
 * - reason: justificación de la acción
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see ModerationActionType
 */
@Entity
@Table(name = "moderation_actions")
public class ModerationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ModerationActionType actionType;

    @Column(name = "moderator_id")
    private Long moderatorId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "target_review_id")
    private Long targetReviewId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reason")
    private String reason;

    public ModerationAction() {}
}