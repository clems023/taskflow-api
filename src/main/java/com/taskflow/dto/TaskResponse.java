package com.taskflow.dto;

import com.taskflow.entity.TaskPriority;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskPriority priority,
        int position,
        LocalDate dueDate,
        UUID projectId,
        UUID columnId,
        UserResponse assignee,
        List<CommentResponse> comments,
        Instant createdAt,
        Instant updatedAt
) {
}
