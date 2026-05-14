package com.example.backend.business.service;

import com.example.backend.business.dto.lesson.LessonRequest;
import com.example.backend.business.dto.lesson.LessonResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService
{
    public LessonResponse getById(Long lessonId)
    {
        return null;
    }

    public List<LessonResponse> getAll()
    {
        return null;
    }

    public LessonResponse create(@Valid LessonRequest request)
    {
        return null;
    }

    public LessonResponse update(Long lessonId, LessonRequest request)
    {
        return null;
    }

    public void delete(Long lessonId)
    {

    }
}
