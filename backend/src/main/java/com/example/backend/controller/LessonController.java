package com.example.backend.controller;

import com.example.backend.business.dto.lesson.LessonRequest;
import com.example.backend.business.dto.lesson.LessonResponse;
import com.example.backend.business.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController
{
    private final LessonService lessonService;

    public LessonController(LessonService lessonService)
    {
        this.lessonService = lessonService;
    }

    @GetMapping("/lesson{lessonId}")
    public ResponseEntity<LessonResponse> getById(@PathVariable Long lessonId) {
        LessonResponse response = lessonService.getById(lessonId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<List<LessonResponse>> getAllByModuleId(@PathVariable Long moduleId) {
        List<LessonResponse> response = lessonService.getAllByModuleId(moduleId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonRequest request) {
        LessonResponse response = lessonService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> update(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonRequest request
    ) {
        LessonResponse response = lessonService.update(lessonId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> delete(@PathVariable Long lessonId) {
        lessonService.delete(lessonId);
        return ResponseEntity.noContent().build();
    }
}