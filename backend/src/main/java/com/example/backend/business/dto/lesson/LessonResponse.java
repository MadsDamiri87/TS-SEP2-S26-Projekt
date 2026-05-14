package com.example.backend.business.dto.lesson;

public record LessonResponse(
        Long lessonId,
        Long moduleId,
        String title,
        String description,
        int orderNumber
){}
