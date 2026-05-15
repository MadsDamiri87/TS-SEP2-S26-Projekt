package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.module.ModuleResponse;
import com.example.backend.entity.Module;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModuleMapper
{
    private final LessonMapper lessonMapper;

    public ModuleMapper(LessonMapper lessonMapper)
    {
        this.lessonMapper = lessonMapper;
    }

    public ModuleResponse toResponse(Module module) {
        return new ModuleResponse(
                module.getModuleId(),
                module.getCourse().getId(),
                module.getName(),
                module.getDescription(),
                module.getOrderNumber(),
                lessonMapper.toResponse(module.getLessons())
        );
    }

    public List<ModuleResponse> toResponse(List<Module> modules) {
        List<ModuleResponse> responses = new ArrayList<>();

        for (Module module : modules) {
            responses.add(toResponse(module));
        }

        return responses;
    }


}
