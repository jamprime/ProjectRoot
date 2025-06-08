package org.ja.project_command.repository;

import org.ja.project_command.entity.EventStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventStoreRepository extends JpaRepository<EventStoreEntity, Long> {
}