package com.example.backend.business.service;

import com.example.backend.business.dto.mapper.ModuleMapper;
import com.example.backend.business.dto.module.ModuleRequest;
import com.example.backend.business.dto.module.ModuleResponse;
import com.example.backend.entity.Module;
import com.example.backend.persistence.repository.ModuleRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService
{
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    public ModuleService(ModuleRepository moduleRepository, ModuleMapper moduleMapper)
    {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
    }

    public ModuleResponse getById(Long moduleId)
    {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Not module found with id=" + moduleId));
        return mo
        return null;
    }

    public List<ModuleResponse> getAll()
    {
        return null;
    }

    public ModuleResponse create(ModuleRequest request)
    {
        return null;
    }

    public ModuleResponse update(Long moduleId, ModuleRequest request)
    {
        return null;
    }

    public void delete(Long moduleId)
    {

    }
}
