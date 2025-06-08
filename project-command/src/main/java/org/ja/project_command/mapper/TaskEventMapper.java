package org.ja.project_command.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ja.project_events.events.TaskEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TaskEventMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String eventToJson(TaskEvent event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    public TaskEvent jsonToEvent(String json, String eventType) throws IOException, ClassNotFoundException {
        return (TaskEvent) objectMapper.readValue(json, Class.forName(eventType));
    }
}