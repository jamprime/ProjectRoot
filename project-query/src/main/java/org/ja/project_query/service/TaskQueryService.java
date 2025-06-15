package org.ja.project_query.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ja.project_query.dto.TaskQueryDto;
import org.ja.project_query.entity.TaskReadEntity;
import org.ja.project_query.mapper.TaskReadMapper;
import org.ja.project_query.repository.TaskReadRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskQueryService {
    private final TaskReadRepository repository;
    private final TaskReadMapper mapper;

    @Cacheable(value = "tasks", key = "#id")
    public TaskQueryDto getTaskById(UUID id) {
        log.info("Cache miss for task: {}. Fetching from database.", id);
        TaskReadEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        log.info("Found task in database: {}", entity);
        return mapper.toDto(entity);
    }

    @Cacheable(value = "activeTasks")
    public ArrayList<TaskQueryDto> getAllTasks() {
        log.info("Cache miss for active tasks. Fetching from database.");
        List<TaskReadEntity> entities = repository.findAllByDeletedFalse();
        log.info("Found {} active tasks in database", entities.size());
        ArrayList<TaskQueryDto> dtos = entities.stream()
                .map(mapper::toDto)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        log.info("Mapped {} tasks to DTOs. Returning from database.", dtos.size());
        return dtos;
    }

    @Cacheable(value = "deletedTasks")
    public ArrayList<TaskQueryDto> getDeletedTasks() {
        log.info("Cache miss for deleted tasks. Fetching from database.");
        List<TaskReadEntity> entities = repository.findByDeletedTrue();
        log.info("Found {} deleted tasks in database", entities.size());
        ArrayList<TaskQueryDto> dtos = entities.stream()
                .map(mapper::toDto)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        log.info("Mapped {} tasks to DTOs. Returning from database.", dtos.size());
        return dtos;
    }
}