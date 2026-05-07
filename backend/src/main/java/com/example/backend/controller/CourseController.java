package com.example.backend.controller;

import com.example.backend.business.dto.course.CourseRequest;
import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.business.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController
{
    private final CourseService courseService;

    public CourseController(CourseService courseService)
    {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/publish/{courseId}")
    public ResponseEntity<CourseResponse> publishCourse(@PathVariable long courseId)
    {
        CourseResponse response = courseService.publishCourse(courseId);
        return ResponseEntity.ok(response);
    }
}
