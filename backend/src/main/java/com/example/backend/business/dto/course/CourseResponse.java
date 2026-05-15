package com.example.backend.business.dto.course;

import com.example.backend.business.dto.module.ModuleResponse;

import java.time.LocalDateTime;
import java.util.List;

public record CourseResponse(Long courseId,
                             Long ownerId,
                             String title,
                             String shortDescription,
                             String description,
                             double price,
                             boolean isPublished,
                             LocalDateTime lastEdited,
                             List<ModuleResponse> modules
                             )
{
}
