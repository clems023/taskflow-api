package com.taskflow.dto;

import java.time.Instant;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String content,
        UserResponse author,
        Instant createdAt
) {
}
