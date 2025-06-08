package org.ja.project_query.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ja.project_query.dto.TaskQueryDto;
import org.ja.project_query.mapper.TaskReadMapper;
import org.ja.project_query.repository.TaskReadRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskQueryService {
    private final TaskReadRepository repository;
    private final TaskReadMapper mapper;

    @Cacheable("tasks")
    public TaskQueryDto getTaskById(UUID id) {
        return repository.findByIdAndDeletedFalse(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    @Cacheable("allTasks")
    public List<TaskQueryDto> getAllTasks() {
        return repository.findAllByDeletedFalse().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable("allTasks")
    public List<TaskQueryDto> getDeletedTasks() {
        return repository.findByDeletedTrue().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}