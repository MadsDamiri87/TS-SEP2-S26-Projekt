package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.enrollment.EnrollmentResponse;
import com.example.backend.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper
{
  public EnrollmentResponse toResponse(Enrollment enrollment)
  {
    return new EnrollmentResponse(
        enrollment.getEnrollmentId(),
        enrollment.getUser().getUserId(),
        enrollment.getCourse().getId(),
        enrollment.getCourse().getTitle(),
        enrollment.getEnrolledAt());
  }
}
