package org.ja.project_events.events;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskUpdatedEvent extends TaskEvent {
    private String title;
    private String description;
    private String status;
} 