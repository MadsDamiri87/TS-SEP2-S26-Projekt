package com.example.backend.business.service;

import com.example.backend.business.dto.mapper.ModuleMapper;
import com.example.backend.business.dto.module.ModuleRequest;
import com.example.backend.business.dto.module.ModuleResponse;
import com.example.backend.entity.Course;
import com.example.backend.entity.Module;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.ModuleRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleService
{
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ModuleMapper moduleMapper;

    public ModuleService(ModuleRepository moduleRepository, CourseRepository courseRepository, ModuleMapper moduleMapper)
    {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
        this.moduleMapper = moduleMapper;
    }

    public ModuleResponse getById(Long moduleId)
    {
        Module module = getModule(moduleId);
        return moduleMapper.toResponse(module);
    }

    public List<ModuleResponse> getAllByCourseId(Long courseId)
    {
        List<Module> modules = moduleRepository.findAllByCourse_Id(courseId);
        return moduleMapper.toResponse(modules);
    }

    public List<ModuleResponse> getAll()
    {
        List<Module> modules = moduleRepository.findAll();
        return moduleMapper.toResponse(modules);
    }

    @Transactional
    public ModuleResponse create(ModuleRequest request)
    {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("No course found with id=" + request.courseId()));

        int orderNumber = moduleRepository.findHighestOrderNumberByCourseId(course.getId()) + 1;

        Module module = new Module();
        module.setCourse(course);
        module.setName(request.name());
        module.setDescription(request.description());
        module.setOrderNumber(orderNumber);

        Module savedModule = moduleRepository.save(module);

        course.setLastEditedToNow();
        courseRepository.save(course);

        return moduleMapper.toResponse(savedModule);
    }

    @Transactional
    public ModuleResponse update(Long moduleId, ModuleRequest request)
    {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("No course found with id=" + request.courseId()));

        Module module = getModule(moduleId);
        module.setName(request.name());
        module.setDescription(request.description());

        Module updatedModule = moduleRepository.save(module);

        course.setLastEditedToNow();
        courseRepository.save(course);

        return moduleMapper.toResponse(updatedModule);
    }

    @Transactional
    public void delete(Long moduleId)
    {
        Module moduleToDelete = getModule(moduleId);
        Long courseId = moduleToDelete.getCourse().getId();
        int orderNumber = moduleToDelete.getOrderNumber();

        moduleRepository.delete(moduleToDelete);

        List<Module> modules = moduleRepository.findAllByCourse_IdAndOrderNumberGreaterThan(courseId, orderNumber);

        for (Module module : modules){
            int currentOrderNumber = module.getOrderNumber();
            module.setOrderNumber(currentOrderNumber - 1);
        }
    }

    private Module getModule(Long moduleId)
    {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("No module found with id=" + moduleId));
    }
}
