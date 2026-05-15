package com.example.backend.business.dto.enrollment;

import java.time.LocalDateTime;

public record EnrollmentResponse(Long enrollmentId,
                                 Long userId,
                                 Long courseId,
                                 String courseTitle,
                                 LocalDateTime enrolledAt)
{
}
