package com.taskflow.dto;

import com.taskflow.entity.Role;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID userId,
        String email,
        String name,
        Role role
) {
}
