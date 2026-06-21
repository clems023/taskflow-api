package com.taskflow.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        UserResponse owner,
        List<UserResponse> members,
        List<BoardColumnResponse> columns,
        Instant createdAt,
        Instant updatedAt
) {
}
