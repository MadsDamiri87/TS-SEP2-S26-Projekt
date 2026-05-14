package com.example.backend.business.service;

import com.example.backend.business.dto.lesson.LessonRequest;
import com.example.backend.business.dto.lesson.LessonResponse;
import com.example.backend.business.dto.mapper.LessonMapper;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.Module;
import com.example.backend.persistence.repository.LessonRepository;
import com.example.backend.persistence.repository.ModuleRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService
{
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final LessonMapper lessonMapper;

    public LessonService(LessonRepository lessonRepository, ModuleRepository moduleRepository, LessonMapper lessonMapper)
    {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
        this.lessonMapper = lessonMapper;
    }

    public LessonResponse getById(Long lessonId)
    {
        Lesson lesson = getLesson(lessonId);
        return lessonMapper.toResponse(lesson);
    }

    public List<LessonResponse> getAllByModuleId(Long moduleId)
    {
        List<Lesson> lessons = lessonRepository.findAllByModule_ModuleId(moduleId);
        return lessonMapper.toResponse(lessons);
    }

    public List<LessonResponse> getAll()
    {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessonMapper.toResponse(lessons);
    }

    public LessonResponse create(LessonRequest request)
    {
        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ResourceNotFoundException("No module with id=" + request.moduleId()));

        int orderNumber = lessonRepository.findHighestOrderNumberByModuleId(module.getModuleId()) + 1;

        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(request.title());
        lesson.setDescription(request.description());
        lesson.setOrderNumber(orderNumber);

        Lesson savedLesson = lessonRepository.save(lesson);

        return lessonMapper.toResponse(savedLesson);
    }

    public LessonResponse update(Long lessonId, LessonRequest request)
    {
        Lesson lesson = getLesson(lessonId);
        lesson.setTitle(request.title());
        lesson.setDescription(request.description());

        Lesson updatedLesson = lessonRepository.save(lesson);

        return lessonMapper.toResponse(updatedLesson);
    }

    public void delete(Long lessonId)
    {
        Lesson lesson = getLesson(lessonId);
        lessonRepository.delete(lesson);
    }

    private Lesson getLesson(Long lessonId)
    {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("No lesson found with id=" + lessonId));
    }
}
