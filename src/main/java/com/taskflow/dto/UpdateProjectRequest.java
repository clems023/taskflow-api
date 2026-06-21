package com.taskflow.dto;

import jakarta.validation.constraints.Size;

public record UpdateProjectRequest(
        @Size(min = 2, max = 100) String name,
        @Size(max = 1000) String description
) {
}
