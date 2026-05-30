package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.moderation.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * Nota*: Actualmente no se utiliza en el código de la aplicación, pero se mantiene para futuras funcionalidades de análisis de auditoría.
 * La tabla de auditoría se gestiona mediante triggers SQL que registran automáticamente las acciones de moderación.
 * -----------------------------------------------------------------------------------------------
 *
 * Repositorio Spring Data JPA para acceder a registros de auditoría.
 *
 * Proporciona operaciones CRUD y consultas personalizadas sobre la entidad {@link com.mara.tfgcine.model.moderation.AuditLog}.
 * Se utiliza para consultar y analizar el histórico de acciones de moderación, pero no para crear nuevos registros
 * (que se generan automáticamente mediante triggers SQL en la base de datos).
 *
 * Métodos personalizados:
 * - findByModeratorId(Long): obtiene todos los registros de auditoría realizados por un moderador específico
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.moderation.AuditLog
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByModeratorId(Long moderatorId
}