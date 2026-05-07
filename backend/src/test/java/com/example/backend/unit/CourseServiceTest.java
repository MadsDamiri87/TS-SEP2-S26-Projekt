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
            //Arrange
            CourseRequest request = validCourseRequest();

            mockUserExists();
            mockCourseSaved();
            mockCourseMappedToResponse();

            //Act
            response = courseService.createCourse(request);
        }

        @Test
        void shouldReturnCourseResponse() {
            // Assert
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithGeneratedId() {
            // Assert
            assertEquals(1L, response.courseId());
        }

        @Test
        void shouldReturnResponseWithTitle() {
            // Assert
            assertEquals("test", response.title());
        }

        @Test
        void shouldReturnResponseWithShortDescription() {
            // Assert
            assertEquals("short description", response.shortDescription());
        }

        @Test
        void shouldReturnResponseWithLongDescription() {
            // Assert
            assertEquals("long description", response.description());
        }

        @Test
        void shouldReturnResponseWithPrice() {
            // Assert
            assertEquals(100.00, response.price());
        }

        @Test
        void shouldFindUserById() {
            // Assert
            verify(userRepository, times(1)).findById(1L);
        }

        @Test
        void shouldSaveCourseOnce() {
            // Assert
            verify(courseRepository, times(1)).save(any(Course.class));
        }

        @Test
        void shouldMapSavedCourseToResponse() {
            // Assert
            verify(courseMapper, times(1)).toResponse(any(Course.class));
        }

        @Test
        void shouldSaveCourseWithCorrectTitle() {
            // Assert
            verify(courseRepository).save(argThat(course ->
                    course.getTitle().equals("test")
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectShortDescription() {
            // Assert
            verify(courseRepository).save(argThat(course ->
                    course.getShortDescription().equals("short description")
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectLongDescription() {
            // Assert
            verify(courseRepository).save(argThat(course ->
                    course.getDescription().equals("long description")
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectPrice() {
            // Assert
            verify(courseRepository).save(argThat(course ->
                    course.getPrice() == 100.00
            ));
        }

        @Test
        void shouldSaveCourseWithCorrectUser() {
            // Assert
            verify(courseRepository).save(argThat(course ->
                    course.getOwner().getId().equals(1L)
            ));
        }
    }

    @Nested
    class CreateCourseWithMissingOwner {

        @Test
        void shouldThrowResourceNotFoundExceptionWhenOwnerDoesNotExist() {
            // Arrange
            CourseRequest request = validCourseRequest();

            when(userRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () ->
                    courseService.createCourse(request)
            );
        }

        @Test
        void shouldNotSaveCourseWhenOwnerDoesNotExist() {
            // Arrange
            CourseRequest request = validCourseRequest();

            when(userRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // Act
            try {
                courseService.createCourse(request);
            } catch (ResourceNotFoundException ignored) {
            }

            // Assert
            verify(courseRepository, never()).save(any(Course.class));
        }

        @Test
        void shouldNotMapCourseWhenOwnerDoesNotExist() {
            // Arrange
            CourseRequest request = validCourseRequest();

            when(userRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // Act
            try {
                courseService.createCourse(request);
            } catch (ResourceNotFoundException ignored) {
            }

            // Assert
            verify(courseMapper, never()).toResponse(any(Course.class));
        }
    }

    private void mockUserExists() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(validUser()));
    }

    private void mockCourseSaved() {
        when(courseRepository.save(any(Course.class)))
                .thenReturn(savedCourse());
    }

    private void mockCourseMappedToResponse() {
        when(courseMapper.toResponse(any(Course.class)))
                .thenReturn(courseResponse());
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
                LocalDateTime.of(2026, 1, 1, 12, 0)
        );
    }

    private User validUser() {
        User user = new User();

        user.setId(1L);

        return user;
    }
}