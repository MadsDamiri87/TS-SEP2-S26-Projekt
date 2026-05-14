package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.lesson.LessonResponse;
import com.example.backend.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper
{
    public LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getLessonId(),
                lesson.getModule().getModuleId(),
                lesson.getTitle(),
                lesson.getDescription()
        );
    }
}
