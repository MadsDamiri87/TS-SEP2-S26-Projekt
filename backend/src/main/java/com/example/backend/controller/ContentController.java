package com.example.backend.controller;

import com.example.backend.business.dto.content.ContentResponse;
import com.example.backend.business.service.ContentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController
{
    private final ContentService contentService;

    public ContentController(ContentService contentService)
    {
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<ContentResponse> upload(
            @RequestParam Long lessonId,
            @RequestParam MultipartFile file
            ) {
        ContentResponse response = contentService.upload(lessonId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ContentResponse> getById(@PathVariable Long contentId){
        ContentResponse response = contentService.getById(contentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<ContentResponse>> getAllByLessonId(@PathVariable Long lessonId){
        List<ContentResponse> responses = contentService.getAllByLessonId(lessonId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{contentId}/file")
    public ResponseEntity<Resource> getFile(@PathVariable Long contentId){
        Resource resource = contentService.getFile(contentId);
        return ResponseEntity.ok(resource);
    }
}
