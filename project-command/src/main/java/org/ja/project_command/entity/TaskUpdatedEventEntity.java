package org.ja.project_command.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("TASK_UPDATED")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdatedEventEntity extends EventStoreEntity {
    private String title;
    private String description;
    private String status;
}