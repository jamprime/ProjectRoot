package org.ja.project_command.mapper;

import lombok.RequiredArgsConstructor;
import org.ja.project_command.entity.*;
import org.ja.project_events.events.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventEntityMapper {

    public EventStoreEntity toEntity(TaskEvent event, UUID aggregateId) {
        if (event instanceof TaskCreatedEvent) {
            TaskCreatedEvent e = (TaskCreatedEvent) event;
            return TaskCreatedEventEntity.builder()
                    .aggregateId(aggregateId)
                    .timestamp(e.getTimestamp())
                    .title(e.getTitle())
                    .description(e.getDescription())
                    .status(e.getStatus())
                    .build();
        } else if (event instanceof TaskUpdatedEvent) {
            TaskUpdatedEvent e = (TaskUpdatedEvent) event;
            return TaskUpdatedEventEntity.builder()
                    .aggregateId(aggregateId)
                    .timestamp(e.getTimestamp())
                    .title(e.getTitle())
                    .description(e.getDescription())
                    .status(e.getStatus())
                    .build();
        } else if (event instanceof TaskDeletedEvent) {
            return TaskDeletedEventEntity.builder()
                    .aggregateId(aggregateId)
                    .timestamp(event.getTimestamp())
                    .build();
        } else if (event instanceof TaskCompletedEvent) {
            return TaskCompletedEventEntity.builder()
                    .aggregateId(aggregateId)
                    .timestamp(event.getTimestamp())
                    .build();
        }
        throw new IllegalArgumentException("Unsupported event type: " + event.getClass().getName());
    }
}