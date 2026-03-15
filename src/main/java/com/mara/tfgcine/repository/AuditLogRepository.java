package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.moderation.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByModeratorId(Long moderatorId

}