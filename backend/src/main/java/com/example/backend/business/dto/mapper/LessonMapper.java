package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.lesson.LessonResponse;
import com.example.backend.entity.Lesson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonMapper
{
    public LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getLessonId(),
                lesson.getModule().getModuleId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getOrderNumber()
        );
    }

    public List<LessonResponse> toResponse(List<Lesson> lessons) {
        List<LessonResponse> responses = new ArrayList<>();

        for (Lesson lesson : lessons) {
            responses.add(toResponse(lesson));
        }

        return responses;
    }
}
