package com.example.backend.business.dto.module;

import com.example.backend.business.dto.lesson.LessonResponse;

import java.util.List;

public record ModuleResponse(
        Long moduleId,
        Long courseId,
        String name,
        String description,
        int orderNumber,
        List<LessonResponse> lessons
){}
