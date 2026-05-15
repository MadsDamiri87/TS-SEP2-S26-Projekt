package com.example.backend.business.dto.enrollment;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
    @NotNull(message = "User-ID is required")
    Long userId,

    @NotNull(message = "Course-ID is required")
    Long courseId
)
{
}
