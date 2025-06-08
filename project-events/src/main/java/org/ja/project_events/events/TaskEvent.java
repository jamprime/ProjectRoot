package org.ja.project_events.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TaskCreatedEvent.class),
    @JsonSubTypes.Type(value = TaskUpdatedEvent.class),
    @JsonSubTypes.Type(value = TaskCompletedEvent.class),
    @JsonSubTypes.Type(value = TaskDeletedEvent.class)
})
public abstract class TaskEvent {
    protected UUID taskId;
    protected LocalDateTime timestamp;

    public LocalDateTime getTimestamp() {
        return timestamp != null ? timestamp : LocalDateTime.now();
    }
} 