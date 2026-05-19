package com.example.backend.business.service;

import com.example.backend.business.dto.lesson.LessonRequest;
import com.example.backend.business.dto.lesson.LessonResponse;
import com.example.backend.business.dto.mapper.LessonMapper;
import com.example.backend.entity.Course;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.Module;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.LessonRepository;
import com.example.backend.persistence.repository.ModuleRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import com.example.backend.shared.util.FileStorageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonService
{
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    public LessonService(LessonRepository lessonRepository,
                         ModuleRepository moduleRepository,
                         CourseRepository courseRepository,
                         LessonMapper lessonMapper)
    {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
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

    @Transactional
    public LessonResponse create(LessonRequest request)
    {
        Module module = getModule(request.moduleId());
        Course course = getCourse(module.getCourse().getId());

        int orderNumber = lessonRepository.findHighestOrderNumberByModuleId(module.getModuleId()) + 1;

        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(request.title());
        lesson.setDescription(request.description());
        lesson.setOrderNumber(orderNumber);

        Lesson savedLesson = lessonRepository.save(lesson);

        course.setLastEditedToNow();
        courseRepository.save(course);

        return lessonMapper.toResponse(savedLesson);
    }

    @Transactional
    public LessonResponse update(Long lessonId, LessonRequest request)
    {
        Module module = getModule(request.moduleId());
        Course course = getCourse(module.getCourse().getId());

        Lesson lesson = getLesson(lessonId);
        lesson.setTitle(request.title());
        lesson.setDescription(request.description());

        Lesson updatedLesson = lessonRepository.save(lesson);

        course.setLastEditedToNow();
        courseRepository.save(course);

        return lessonMapper.toResponse(updatedLesson);
    }

    @Transactional
    public void delete(Long lessonId)
    {
        Lesson lesson = getLesson(lessonId);

        Long moduleId = lesson.getModule().getModuleId();
        int deletedOrderNumber = lesson.getOrderNumber();

        FileStorageHelper.deleteLessonContentFilesAndFolder(lesson.getContents());

        lessonRepository.delete(lesson);
        lessonRepository.flush();

        List<Lesson> lessonsToShift = lessonRepository
                .findAllByModule_ModuleIdAndOrderNumberGreaterThan(moduleId, deletedOrderNumber);

        for (Lesson lessonToShift : lessonsToShift) {
            lessonToShift.setOrderNumber(lessonToShift.getOrderNumber() - 1);
        }

        lessonRepository.saveAll(lessonsToShift);
    }

    private Lesson getLesson(Long lessonId)
    {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("No lesson found with id=" + lessonId));
    }

    private Module getModule(Long moduleId)
    {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("No module found with id=" + moduleId));
    }

    private Course getCourse(Long courseId)
    {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("No course found with id=" + courseId));
    }
}
