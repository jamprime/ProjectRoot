package org.ja.project_query.mapper;

import org.ja.project_query.dto.TaskQueryDto;
import org.ja.project_query.entity.TaskReadEntity;
import org.ja.project_events.events.TaskCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskReadMapper {
    TaskQueryDto toDto(TaskReadEntity entity);

    @Mapping(target = "id", source = "taskId")
    @Mapping(target = "createdAt", source = "timestamp")
    @Mapping(target = "updatedAt", source = "timestamp")
    TaskReadEntity toEntity(TaskCreatedEvent event);
}