package org.ja.project_command.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ja.project_command.dto.TaskCommandDto;
import org.ja.project_command.service.TaskCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Command API", description = "Управление операциями с задачами")
@Slf4j
public class TaskCommandController {
    private final TaskCommandService taskService;

    @Operation(
            summary = "Создать задачу",
            description = "Создаёт новую задачу и возвращает её UUID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача успешно создана",
                    content = @Content(schema = @Schema(implementation = UUID.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка сериализации/отправки события",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<UUID> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания задачи",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TaskCommandDto.class))
            )
            @RequestBody TaskCommandDto dto
    ) {
        try {
            UUID taskId = taskService.createTask(dto);
            return ResponseEntity.ok(taskId);
        } catch (Exception e) {
            log.info(String.valueOf(e));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Event processing error",
                    e
            );
        }
    }

    @Operation(
            summary = "Обновить задачу",
            description = "Обновляет данные существующей задачи"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача успешно обновлена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задача не найдена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка сериализации/отправки события",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(
            @Parameter(
                    description = "UUID задачи",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновлённые данные задачи",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TaskCommandDto.class))
            )
            @RequestBody TaskCommandDto dto
    ) {
        try {
            taskService.updateTask(id, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info(String.valueOf(e));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Event processing error",
                    e
            );
        }
    }

    @Operation(
            summary = "Завершить задачу",
            description = "Помечает задачу как завершённую"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача успешно завершена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задача не найдена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка сериализации/отправки события",
                    content = @Content
            )
    })
    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeTask(
            @Parameter(
                    description = "UUID завершаемой задачи",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID id
    ) {
        try {
            taskService.completeTask(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info(String.valueOf(e));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Event processing error",
                    e
            );
        }
    }

    @Operation(
            summary = "Удалить задачу",
            description = "Помечает задачу как удалённую (soft delete)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача помечена как удалённая"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задача не найдена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка сериализации/отправки события",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(
                    description = "UUID удаляемой задачи",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID id
    ) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Event processing error",
                    e
            );
        }
    }
}