package com.taskflow.dto;

import java.time.Instant;
import java.util.UUID;

public record BoardColumnResponse(
        UUID id,
        String name,
        int position,
        Instant createdAt
) {
}
