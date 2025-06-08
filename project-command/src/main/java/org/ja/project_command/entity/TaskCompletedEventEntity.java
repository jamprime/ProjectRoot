package org.ja.project_command.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("TASK_COMPLETED")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TaskCompletedEventEntity extends EventStoreEntity {}