package com.taskflow.dto;

import com.taskflow.entity.TaskPriority;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskRequest(
        @Size(min = 2, max = 200) String title,
        @Size(max = 2000) String description,
        TaskPriority priority,
        UUID columnId,
        UUID assigneeId,
        LocalDate dueDate,
        Integer position
) {
}
