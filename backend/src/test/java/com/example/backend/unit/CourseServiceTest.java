package com.example.backend.unit;

import com.example.backend.business.dto.course.CourseRequest;
import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.business.dto.mapper.CourseMapper;
import com.example.backend.business.service.CourseService;
import com.example.backend.entity.Course;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Nested
    class CreateCourseWithValidInput {

        private CourseResponse response;

        @BeforeEach
        void setup() {
            // Arrange
            CourseRequest request = validCourseRequest();

            mockUserExists();
            mockCourseSaved();
            mockCourseMappedToResponse();

            // Act
            response = courseService.createCourse(request);
        }

        @Test
        void shouldReturnCourseResponse() {
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithGeneratedId() {
            assertEquals(1L, response.courseId());
        }

        @Test
        void shouldReturnResponseWithTitle() {
            assertEquals("test", response.title());
        }

        @Test
        void shouldReturnResponseWithShortDescription() {
            assertEquals("short description", response.shortDescription());
        }

        @Test
        void shouldReturnResponseWithLongDescription() {
            assertEquals("long description", response.description());
        }

        @Test
        void shouldReturnResponseWithPrice() {
            assertEquals(100.00, response.price(), 0.001);
        }

        @Test
        void shouldFindUserById() {
            verify(userRepository, times(1)).findById(1L);
        }

        @Test
        void shouldSaveCourseOnce() {
            verify(courseRepository, times(1)).save(any(Course.class));
        }

        @Test
        void shouldMapSavedCourseToResponse() {
            verify(courseMapper, times(1)).toResponse(any(Course.class));
        }

        @Test
        void shouldSaveCourseWithCorrectTitle() {
            verify(courseRepository).save(argThat(course ->
                    course.getTitle().equals("test")
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectShortDescription() {
            verify(courseRepository).save(argThat(course ->
                    course.getShortDescription().equals("short description")
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectLongDescription() {
            verify(courseRepository).save(argThat(course ->
                    course.getDescription().equals("long description")
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectPrice() {
            verify(courseRepository).save(argThat(course ->
                    Math.abs(course.getPrice() - 100.00) < 0.001
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectOwner() {
            verify(courseRepository).save(argThat(course ->
                    course.getOwner().getUserId().equals(1L)
            ));
        }

        @Test
        void shouldSaveCourseAsUnpublished() {
            verify(courseRepository).save(argThat(course ->
                    !course.isPublished()
            ));
        }
    }

    @Nested
    class CreateCourseWithMissingOwner {

        @BeforeEach
        void setup() {
            // Arrange
            mockUserDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenOwnerDoesNotExist() {
            // Arrange
            CourseRequest request = validCourseRequest();

            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () ->
                    courseService.createCourse(request)
            );
        }

        @Test
        void shouldNotSaveCourseWhenOwnerDoesNotExist() {
            // Act
            createCourseIgnoringResourceNotFoundException();

            // Assert
            verify(courseRepository, never()).save(any(Course.class));
        }

        @Test
        void shouldNotMapCourseWhenOwnerDoesNotExist() {
            // Act
            createCourseIgnoringResourceNotFoundException();

            // Assert
            verify(courseMapper, never()).toResponse(any(Course.class));
        }
    }

    @Nested
    class PublishCourseWithValidInput {

        private CourseResponse response;

        @BeforeEach
        void setup() {
            // Arrange
            mockCourseExists();
            mockCourseSaved();
            mockCourseMappedToResponse();

            // Act
            response = courseService.publishCourse(1L);
        }

        @Test
        void shouldReturnCourseResponse() {
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithGeneratedId() {
            assertEquals(1L, response.courseId());
        }

        @Test
        void shouldFindCourseById() {
            verify(courseRepository, times(1)).findById(1L);
        }

        @Test
        void shouldSaveCourseOnce() {
            verify(courseRepository, times(1)).save(any(Course.class));
        }

        @Test
        void shouldSaveCourseAsPublished() {
            verify(courseRepository).save(argThat(Course::isPublished));
        }

        @Test
        void shouldMapSavedCourseToResponse() {
            verify(courseMapper, times(1)).toResponse(any(Course.class));
        }
    }

    @Nested
    class PublishCourseWithMissingCourse {

        @BeforeEach
        void setup() {
            // Arrange
            mockCourseDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenCourseDoesNotExist() {
            assertThrows(ResourceNotFoundException.class, () ->
                    courseService.publishCourse(1L)
            );
        }

        @Test
        void shouldNotSaveCourseWhenCourseDoesNotExist() {
            // Act
            publishCourseIgnoringResourceNotFoundException();

            // Assert
            verify(courseRepository, never()).save(any(Course.class));
        }

        @Test
        void shouldNotMapCourseWhenCourseDoesNotExist() {
            // Act
            publishCourseIgnoringResourceNotFoundException();

            // Assert
            verify(courseMapper, never()).toResponse(any(Course.class));
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
                .thenReturn(Optional.of(savedCourse()));
    }

    private void mockCourseDoesNotExist() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    private void mockCourseSaved() {
        when(courseRepository.save(any(Course.class)))
                .thenReturn(savedCourse());
    }

    private void mockCourseMappedToResponse() {
        when(courseMapper.toResponse(any(Course.class)))
                .thenReturn(courseResponse());
    }

    private void createCourseIgnoringResourceNotFoundException() {
        try {
            courseService.createCourse(validCourseRequest());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private void publishCourseIgnoringResourceNotFoundException() {
        try {
            courseService.publishCourse(1L);
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private CourseRequest validCourseRequest() {
        return new CourseRequest(
                1L,
                "test",
                "short description",
                "long description",
                100.00
        );
    }

    private Course savedCourse() {
        Course course = new Course();

        course.setId(1L);
        course.setTitle("test");
        course.setShortDescription("short description");
        course.setDescription("long description");
        course.setPrice(100.00);
        course.setOwner(validUser());

        return course;
    }

    private CourseResponse courseResponse() {
        return new CourseResponse(
                1L,
                1L,
                "test",
                "short description",
                "long description",
                100.00,
                false,
                LocalDateTime.of(2026, 1, 1, 12, 0)
        );
    }

    private User validUser() {
        User user = new User();

        user.setUserId(1L);

        return user;
    }


}