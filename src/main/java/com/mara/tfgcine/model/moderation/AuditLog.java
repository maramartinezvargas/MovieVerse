package com.mara.tfgcine.model.moderation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * NOTA: Esta funcionalidad actualmente NO SE UTILIZA desde la aplicación Java.
 * La auditoría se gestiona mediante trigger SQL que registran automáticamente las acciones
 * en la tabla {@code audit_logs}.
 * -----------------------------------------------------------------------------------------------
 *
 * Entidad JPA que registra acciones realizadas por moderadores para auditoría.
 *
 * Cada instancia representa un evento de moderación (por ejemplo: rechazo de un reporte,
 * eliminación de una reseña, asignación de moderador, etc.) y se persiste en la tabla
 * {@code audit_logs} para trazabilidad y análisis posteriores.
 *
 * Campos principales:
 * - moderatorId: identificador del moderador que ejecutó la acción.
 * - action: código o descripción breve de la acción realizada (p. ej. "REJECT_REPORT").
 * - reportId: identificador del reporte afectado.
 * - timestamp: instante en que se produjo la acción.
 * - details: texto adicional con contexto (motivo, datos relevantes, JSON, etc.).
 *
 * Uso típico: almacenar un histórico inmutable de acciones de moderación
 * para poder auditar comportamientos, depurar incidencias o generar estadísticas.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 */

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "moderator_id")
    private Long moderatorId;

    @Column(name = "action")
    private String action;

    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "details")
    private String details;
}