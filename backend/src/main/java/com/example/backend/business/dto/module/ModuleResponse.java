package com.example.backend.business.dto.module;

public record ModuleResponse(
        Long moduleId,
        Long courseId,
        String name,
        String description,
        int orderNumber
){}
