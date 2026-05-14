package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.module.ModuleResponse;
import com.example.backend.entity.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper
{
    public ModuleResponse toResponse(Module module) {
        return new ModuleResponse(
                module.getModuleId(),
                module.getCourse().getId(),
                module.getName(),
                module.getDescription()
        );
    }
}
