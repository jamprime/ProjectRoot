package org.ja.project_events.events;

import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskDeletedEvent extends TaskEvent {
} 