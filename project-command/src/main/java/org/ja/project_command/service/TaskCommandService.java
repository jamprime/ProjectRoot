package org.ja.project_command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ja.project_events.events.*;
import org.ja.project_command.dto.TaskCommandDto;
import org.ja.project_command.entity.EventStoreEntity;
import org.ja.project_command.mapper.EventEntityMapper;
import org.ja.project_command.repository.EventStoreRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCommandService {
    private final EventStoreRepository eventStoreRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EventEntityMapper eventEntityMapper;
    @Value("${rabbitmq.queue.tasks_name}")
    private String tasksQueueName;

    @Transactional
    public UUID createTask(TaskCommandDto dto) {
        try {
            UUID taskId = UUID.randomUUID();
            TaskCreatedEvent event = TaskCreatedEvent
                    .builder()
                    .taskId(taskId)
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .status("CREATED")
                    .timestamp(LocalDateTime.now())
                    .build();
            saveAndPublishEvent(event, taskId);
            log.info("Created and published TaskCreatedEvent for task: {}", taskId);
            return taskId;
        } catch (Exception e) {
            log.error("Error creating task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create task", e);
        }
    }

    @Transactional
    public void updateTask(UUID taskId, TaskCommandDto dto) {
        try {
            TaskUpdatedEvent event = TaskUpdatedEvent
                    .builder()
                    .taskId(taskId)
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .status("UPDATED")
                    .timestamp(LocalDateTime.now())
                    .build();
            saveAndPublishEvent(event, taskId);
            log.info("Created and published TaskUpdatedEvent for task: {}", taskId);
        } catch (Exception e) {
            log.error("Error updating task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update task", e);
        }
    }

    @Transactional
    public void completeTask(UUID taskId) {
        try {
            TaskCompletedEvent event = TaskCompletedEvent.builder()
                    .taskId(taskId)
                    .timestamp(LocalDateTime.now())
                    .build();
            saveAndPublishEvent(event, taskId);
            log.info("Created and published TaskCompletedEvent for task: {}", taskId);
        } catch (Exception e) {
            log.error("Error completing task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to complete task", e);
        }
    }

    @Transactional
    public void saveAndPublishEvent(TaskEvent event, UUID aggregateId) {
        try {
            EventStoreEntity entity = eventEntityMapper.toEntity(event, aggregateId);
            eventStoreRepository.save(entity);
            log.info("Saved event to event store: {} for aggregate: {}", event.getClass().getSimpleName(), aggregateId);

            rabbitTemplate.convertAndSend(
                    tasksQueueName,
                    event
            );
            log.info("Published event to RabbitMQ: {} for aggregate: {}", event.getClass().getSimpleName(), aggregateId);
        } catch (Exception e) {
            log.error("Error saving and publishing event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save and publish event", e);
        }
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        try {
            TaskDeletedEvent event = TaskDeletedEvent.builder()
                    .taskId(taskId)
                    .timestamp(LocalDateTime.now())
                    .build();
            saveAndPublishEvent(event, taskId);
            log.info("Created and published TaskDeletedEvent for task: {}", taskId);
        } catch (Exception e) {
            log.error("Error deleting task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete task", e);
        }
    }
}