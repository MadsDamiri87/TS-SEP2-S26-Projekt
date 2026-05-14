package com.example.backend.business.dto.lesson;

public record LessonResponse(
        Long lessonId,
        Long moduleId,
        String name,
        String description
){}
