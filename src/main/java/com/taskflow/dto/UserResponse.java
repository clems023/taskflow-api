package com.taskflow.dto;

import com.taskflow.entity.Role;
import com.taskflow.entity.TaskPriority;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String name,
        Role role,
        Instant createdAt
) {
}
