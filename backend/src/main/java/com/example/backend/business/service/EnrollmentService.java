package com.example.backend.business.service;

import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.business.dto.enrollment.EnrollmentRequest;
import com.example.backend.business.dto.enrollment.EnrollmentResponse;
import com.example.backend.business.dto.mapper.CourseMapper;
import com.example.backend.business.dto.mapper.EnrollmentMapper;
import com.example.backend.entity.Course;
import com.example.backend.entity.Enrollment;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.EnrollmentRepository;
import com.example.backend.persistence.repository.UserRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService
{

  private final EnrollmentRepository enrollmentRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final EnrollmentMapper enrollmentMapper;
  private final CourseMapper courseMapper;

  public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository,
                           CourseRepository courseRepository, EnrollmentMapper enrollmentMapper,
                           CourseMapper courseMapper)
  {
    this.enrollmentRepository = enrollmentRepository;
    this.userRepository       = userRepository;
    this.courseRepository     = courseRepository;
    this.enrollmentMapper     = enrollmentMapper;
    this.courseMapper         = courseMapper;
  }

  @Transactional
  public EnrollmentResponse enrollUserInCourse(EnrollmentRequest request)
  {
    User user = userRepository.findById(request.userId())
                              .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Course course = courseRepository.findById(request.courseId()).orElseThrow(
        () -> new ResourceNotFoundException("Course not found"));

    Enrollment enrollment = new Enrollment();
    enrollment.setUser(user);
    enrollment.setCourse(course);
    enrollment.setEnrolledAt();

    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

    user.setCourseParticipant(true);
    userRepository.save(user);

    return enrollmentMapper.toResponse(savedEnrollment);
  }

  public List<CourseResponse> getEnrollmentsForUser(long userId)
  {
    User user = userRepository.findById(userId)
                              .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    List<Course> boughtCourses = enrollmentRepository.findByUser_UserId(user.getUserId()).stream()
                                                     .map(Enrollment::getCourse).toList();

    return courseMapper.toResponse(boughtCourses);
  }
}
