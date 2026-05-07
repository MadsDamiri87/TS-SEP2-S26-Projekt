package com.example.backend.business.dto.course;

import java.time.LocalDateTime;

public record CourseResponse(Long courseId,
                             Long ownerId,
                             String title,
                             String shortDescription,
                             String description,
                             double price,
                             boolean isPublished,
                             LocalDateTime lastEdited)
{
}
