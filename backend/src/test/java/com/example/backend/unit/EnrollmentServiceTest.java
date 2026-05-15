package com.example.backend.unit;

import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.business.dto.enrollment.EnrollmentRequest;
import com.example.backend.business.dto.enrollment.EnrollmentResponse;
import com.example.backend.business.dto.mapper.CourseMapper;
import com.example.backend.business.dto.mapper.EnrollmentMapper;
import com.example.backend.business.service.EnrollmentService;
import com.example.backend.entity.Course;
import com.example.backend.entity.Enrollment;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.EnrollmentRepository;
import com.example.backend.persistence.repository.UserRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) public class EnrollmentServiceTest
{
  @Mock private EnrollmentRepository enrollmentRepository;

  @Mock private UserRepository userRepository;

  @Mock private CourseRepository courseRepository;

  @Mock EnrollmentMapper enrollmentMapper;

  @Mock CourseMapper courseMapper;

  @InjectMocks private EnrollmentService enrollmentService;

  @Nested class EnrollUserInCourseWithValidInput
  {
    private EnrollmentResponse response;

    @BeforeEach void setup()
    {
      mockUserExists();
      mockCourseExists();
      mockEnrollmentSaved();
      mockEnrollmentMappedToResponse();

      response = enrollmentService.enrollUserInCourse(validEnrollmentRequest());
    }

    @Test void shouldReturnEnrollmentResponse()
    {
      assertNotNull(response);
    }

    @Test void shouldFindUserById()
    {
      verify(userRepository, times(1)).findById(1L);

    }

    @Test void shouldFIndCourseById()
    {
      verify(courseRepository, times(1)).findById(1L);
    }

    @Test void shouldSaveEnrollmentOnce()
    {
      verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test void shouldSaveEnrollmentWithCorrectCourse()
    {
      verify(enrollmentRepository).save(
          argThat(enrollment -> enrollment.getCourse().getId().equals(1L)));
    }

    @Test void shouldSetUserAsCourseParticipant()
    {
      verify(userRepository).save(argThat(user -> user.isCourseParticipant()));
    }

    @Test void shouldSaveUserOnce()
    {
      verify(userRepository, times(1)).save(any(User.class));
    }

    @Test void shouldMapSavedEnrollmentToResponse()
    {
      verify(enrollmentMapper, times(1)).toResponse(any(Enrollment.class));
    }
  }
  @Nested
  class EnrollUserInCourseWithMissingUser {

    @BeforeEach
    void setup() {
      mockUserDoesNotExist();
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
      assertThrows(ResourceNotFoundException.class, () ->
          enrollmentService.enrollUserInCourse(validEnrollmentRequest())
      );
    }

    @Test
    void shouldNotFindCourseWhenUserDoesNotExist() {
      enrollIgnoringResourceNotFoundException();

      verify(courseRepository, never()).findById(anyLong());
    }

    @Test
    void shouldNotSaveEnrollmentWhenUserDoesNotExist() {
      enrollIgnoringResourceNotFoundException();

      verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void shouldNotMapEnrollmentWhenUserDoesNotExist() {
      enrollIgnoringResourceNotFoundException();

      verify(enrollmentMapper, never()).toResponse(any(Enrollment.class));
    }
  }

  @Nested
  class EnrollUserInCourseWithMissingCourse {

    @BeforeEach
    void setup() {
      mockUserExists();
      mockCourseDoesNotExist();
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCourseDoesNotExist() {
      assertThrows(ResourceNotFoundException.class, () ->
          enrollmentService.enrollUserInCourse(validEnrollmentRequest())
      );
    }

    @Test
    void shouldNotSaveEnrollmentWhenCourseDoesNotExist() {
      enrollIgnoringResourceNotFoundException();

      verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void shouldNotSaveUserWhenCourseDoesNotExist() {
      enrollIgnoringResourceNotFoundException();

      verify(userRepository, never()).save(argThat(User::isCourseParticipant));
    }

    @Test
    void shouldNotMapEnrollmentWhenCourseDoesNotExist() {
      enrollIgnoringResourceNotFoundException();

      verify(enrollmentMapper, never()).toResponse(any(Enrollment.class));
    }
  }

  @Nested
  class GetEnrollmentsForUserWithValidInput {

    private List<CourseResponse> response;

    @BeforeEach
    void setup() {
      mockUserExists();
      mockEnrollmentsFound();
      mockCoursesMappedToResponse();

      response = enrollmentService.getEnrollmentsForUser(1L);
    }

    @Test
    void shouldReturnCourseResponses() {
      assertNotNull(response);
    }

    @Test
    void shouldReturnOneCourseResponse() {
      assertEquals(1, response.size());
    }

    @Test
    void shouldFindUserById() {
      verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldFindEnrollmentsByUserId() {
      verify(enrollmentRepository, times(1)).findByUser_UserId(1L);
    }

    @Test
    void shouldMapCoursesToResponse() {
      verify(courseMapper, times(1)).toResponse(anyList());
    }
  }

  @Nested
  class GetEnrollmentsForUserWithMissingUser {

    @BeforeEach
    void setup() {
      mockUserDoesNotExist();
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
      assertThrows(ResourceNotFoundException.class, () ->
          enrollmentService.getEnrollmentsForUser(1L)
      );
    }

    @Test
    void shouldNotFindEnrollmentsWhenUserDoesNotExist() {
      getEnrollmentsIgnoringResourceNotFoundException();

      verify(enrollmentRepository, never()).findByUser_UserId(anyLong());
    }

    @Test
    void shouldNotMapCoursesWhenUserDoesNotExist() {
      getEnrollmentsIgnoringResourceNotFoundException();

      verify(courseMapper, never()).toResponse(anyList());
    }
  }

  private void mockUserExists() {
    when(userRepository.findById(1L))
        .thenReturn(Optional.of(validUser()));
  }

  private void mockUserDoesNotExist() {
    when(userRepository.findById(1L))
        .thenReturn(Optional.empty());
  }

  private void mockCourseExists() {
    when(courseRepository.findById(1L))
        .thenReturn(Optional.of(validCourse()));
  }

  private void mockCourseDoesNotExist() {
    when(courseRepository.findById(1L))
        .thenReturn(Optional.empty());
  }

  private void mockEnrollmentSaved() {
    when(enrollmentRepository.save(any(Enrollment.class)))
        .thenReturn(savedEnrollment());
  }

  private void mockEnrollmentMappedToResponse() {
    when(enrollmentMapper.toResponse(any(Enrollment.class)))
        .thenReturn(enrollmentResponse());
  }

  private void mockEnrollmentsFound() {
    when(enrollmentRepository.findByUser_UserId(1L))
        .thenReturn(List.of(savedEnrollment()));
  }

  private void mockCoursesMappedToResponse() {
    when(courseMapper.toResponse(anyList()))
        .thenReturn(List.of(courseResponse()));
  }

  private void enrollIgnoringResourceNotFoundException() {
    try {
      enrollmentService.enrollUserInCourse(validEnrollmentRequest());
    } catch (ResourceNotFoundException ignored) {
    }
  }

  private void getEnrollmentsIgnoringResourceNotFoundException() {
    try {
      enrollmentService.getEnrollmentsForUser(1L);
    } catch (ResourceNotFoundException ignored) {
    }
  }

  private EnrollmentRequest validEnrollmentRequest() {
    return new EnrollmentRequest(1L, 1L);
  }

  private Enrollment savedEnrollment() {
    Enrollment enrollment = new Enrollment();

    enrollment.setEnrollmentId(1L);
    enrollment.setUser(validUser());
    enrollment.setCourse(validCourse());
    enrollment.setEnrolledAt();

    return enrollment;
  }

  private EnrollmentResponse enrollmentResponse() {
    return new EnrollmentResponse(
        1L,
        1L,
        1L,
        "test",
        LocalDateTime.of(2026, 1, 1, 12, 0)
    );
  }

  private CourseResponse courseResponse() {
    return new CourseResponse(
        1L,
        1L,
        "test",
        "short description",
        "long description",
        100.00,
        true,
        LocalDateTime.of(2026, 1, 1, 12, 0),
        new ArrayList<>()
    );
  }

  private User validUser() {
    User user = new User();
    user.setUserId(1L);
    return user;
  }

  private Course validCourse() {
    Course course = new Course();
    course.setId(1L);
    course.setTitle("test");
    course.setShortDescription("short description");
    course.setDescription("long description");
    course.setPrice(100.00);
    return course;
  }
}

