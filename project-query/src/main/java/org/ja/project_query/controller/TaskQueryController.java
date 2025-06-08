package org.ja.project_query.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ja.project_query.dto.TaskQueryDto;
import org.ja.project_query.service.TaskQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Query API", description = "Управление запросами задач")
public class TaskQueryController {
    private final TaskQueryService taskService;

    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает активную задачу по идентификатору. Удалённые задачи не возвращаются."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача найдена",
                    content = @Content(schema = @Schema(implementation = TaskQueryDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задача не найдена или удалена",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskQueryDto> getTask(
            @Parameter(description = "UUID задачи", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable UUID id
    ) {
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

    @Operation(
            summary = "Получить все активные задачи",
            description = "Возвращает список всех неудалённых задач"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список активных задач",
            content = @Content(schema = @Schema(implementation = TaskQueryDto[].class))
    )
    @GetMapping
    public ResponseEntity<List<TaskQueryDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(
            summary = "Получить удалённые задачи",
            description = "Возвращает список задач, помеченных как удалённые"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список удалённых задач",
            content = @Content(schema = @Schema(implementation = TaskQueryDto[].class))
    )
    @GetMapping("/deleted")
    public ResponseEntity<List<TaskQueryDto>> getDeletedTasks() {
        return ResponseEntity.ok(taskService.getDeletedTasks());
    }
}