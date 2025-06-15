package org.ja.project_query.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ja.project_query.entity.TaskReadEntity;
import org.ja.project_events.events.*;
import org.ja.project_query.mapper.TaskReadMapper;
import org.ja.project_query.repository.TaskReadRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventHandler {
    private final TaskReadRepository repository;
    private final TaskReadMapper mapper;

    @RabbitListener(queues = "${rabbitmq.queue.tasks_name}")
    @Transactional
    @CacheEvict(value = {"tasks", "activeTasks", "deletedTasks"}, allEntries = true)
    public void handleTaskEvent(TaskEvent event) {
        log.info("Received event: {} with type: {}", event, event.getClass().getSimpleName());
        try {
            if (event instanceof TaskCreatedEvent) {
                handleCreate((TaskCreatedEvent) event);
            } else if (event instanceof TaskUpdatedEvent) {
                handleUpdate((TaskUpdatedEvent) event);
            } else if (event instanceof TaskCompletedEvent) {
                handleComplete((TaskCompletedEvent) event);
            } else if (event instanceof TaskDeletedEvent) {
                handleDelete((TaskDeletedEvent) event);
            }
            log.info("Successfully processed event: {}", event.getTaskId());
        } catch (Exception e) {
            log.error("Error processing event: {}", event.getTaskId(), e);
            throw new RuntimeException("Failed to process event", e);
        }
    }

    private void handleCreate(TaskCreatedEvent event) {
        log.info("Handling TaskCreatedEvent for task: {}", event.getTaskId());
        TaskReadEntity entity = mapper.toEntity(event);
        entity.setCreatedAt(LocalDateTime.now());
        repository.save(entity);
        log.info("Created task in read model: {}", entity.getId());
    }

    private void handleUpdate(TaskUpdatedEvent event) {
        log.info("Handling TaskUpdatedEvent for task: {}", event.getTaskId());
        repository.findById(event.getTaskId()).ifPresent(entity -> {
            entity.setTitle(event.getTitle());
            entity.setDescription(event.getDescription());
            entity.setStatus(event.getStatus());
            entity.setUpdatedAt(LocalDateTime.now());
            repository.save(entity);
            log.info("Updated task in read model: {}", entity.getId());
        });
    }

    private void handleComplete(TaskCompletedEvent event) {
        log.info("Handling TaskCompletedEvent for task: {}", event.getTaskId());
        repository.findById(event.getTaskId()).ifPresent(entity -> {
            entity.setStatus("COMPLETED");
            entity.setUpdatedAt(LocalDateTime.now());
            repository.save(entity);
            log.info("Completed task in read model: {}", entity.getId());
        });
    }

    private void handleDelete(TaskDeletedEvent event) {
        log.info("Handling TaskDeletedEvent for task: {}", event.getTaskId());
        repository.findById(event.getTaskId()).ifPresent(entity -> {
            entity.setDeleted(true);
            entity.setUpdatedAt(LocalDateTime.now());
            repository.save(entity);
            log.info("Marked task as deleted in read model: {}", entity.getId());
        });
    }
}