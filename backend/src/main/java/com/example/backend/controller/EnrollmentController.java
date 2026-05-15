package com.example.backend.controller;

import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.business.dto.enrollment.EnrollmentRequest;
import com.example.backend.business.dto.enrollment.EnrollmentResponse;
import com.example.backend.business.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController
{
  private final EnrollmentService enrollmentService;

  public EnrollmentController(EnrollmentService enrollmentService)
  {
    this.enrollmentService = enrollmentService;
  }

  @PostMapping
  public ResponseEntity<EnrollmentResponse> enroll(
      @Valid @RequestBody EnrollmentRequest request)
  {
    EnrollmentResponse response = enrollmentService.enrollUserInCourse(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }


  @GetMapping("/{userId}")
  public ResponseEntity<List<CourseResponse>> getAllEnrolledCoursesForUser(@PathVariable Long userId) {
    List<CourseResponse> responses = enrollmentService.getEnrollmentsForUser(userId);
    return ResponseEntity.ok(responses);
  }

}
