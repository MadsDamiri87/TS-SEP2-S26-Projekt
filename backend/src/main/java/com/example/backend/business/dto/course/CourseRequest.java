package com.example.backend.business.dto.course;

import jakarta.validation.constraints.*;

public record CourseRequest(
        @NotNull(message = "Owner id is required")
        Long ownerId,

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot be longer than 100 characters")
        String title,

        @NotBlank(message = "Short description is required")
        @Size(max = 255, message = "Short description cannot be longer than 255 characters")
        String shortDescription,

        @NotBlank(message = "Description is required")
        @Size(max = 5000, message = "Description cannot be longer than 5000 characters")
        String description,

        @PositiveOrZero(message = "Price must be positive or zero")
        @NotNull(message = "Price is required")
        Double price
)
{}
