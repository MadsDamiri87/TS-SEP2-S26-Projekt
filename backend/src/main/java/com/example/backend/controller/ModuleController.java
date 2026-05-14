package com.example.backend.controller;

import com.example.backend.business.dto.module.ModuleRequest;
import com.example.backend.business.dto.module.ModuleResponse;
import com.example.backend.business.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController
{
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService)
    {
        this.moduleService = moduleService;
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> getById(@PathVariable Long moduleId) {
        ModuleResponse response = moduleService.getById(moduleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ModuleResponse>> getAll() {
        List<ModuleResponse> response = moduleService.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<ModuleResponse> create(@Valid @RequestBody ModuleRequest request) {
        ModuleResponse response = moduleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> update(
            @PathVariable Long moduleId,
            @Valid @RequestBody ModuleRequest request){
        ModuleResponse response = moduleService.update(moduleId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> delete(@PathVariable Long moduleId){
        moduleService.delete(moduleId);
        return ResponseEntity.noContent().build();
    }
}
