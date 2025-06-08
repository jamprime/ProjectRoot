package org.ja.project_query.repository;

import org.ja.project_query.entity.TaskReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskReadRepository extends JpaRepository<TaskReadEntity, UUID> {
    Optional<TaskReadEntity> findByIdAndDeletedFalse(UUID id);
    List<TaskReadEntity> findAllByDeletedFalse();
    List<TaskReadEntity> findByDeletedTrue();
}