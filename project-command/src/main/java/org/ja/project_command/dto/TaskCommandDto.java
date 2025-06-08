package org.ja.project_command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCommandDto {
    private String title;
    private String description;
    private String status;
}