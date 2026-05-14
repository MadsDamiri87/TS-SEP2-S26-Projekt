package com.example.backend.unit;

import com.example.backend.business.dto.lesson.LessonRequest;
import com.example.backend.business.dto.lesson.LessonResponse;
import com.example.backend.business.dto.mapper.LessonMapper;
import com.example.backend.business.service.LessonService;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.Module;
import com.example.backend.persistence.repository.LessonRepository;
import com.example.backend.persistence.repository.ModuleRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonService lessonService;

    @Nested
    class CreateLessonWithValidInput {

        private LessonResponse response;

        @BeforeEach
        void setup() {
            // Arrange
            LessonRequest request = validLessonRequest();

            mockModuleExists();
            mockLessonSaved();
            mockLessonMappedToResponse();

            // Act
            response = lessonService.create(request);
        }

        @Test
        void shouldReturnLessonResponse() {
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithGeneratedId() {
            assertEquals(1L, response.lessonId());
        }

        @Test
        void shouldReturnResponseWithModuleId() {
            assertEquals(1L, response.moduleId());
        }

        @Test
        void shouldReturnResponseWithTitle() {
            assertEquals("Lesson 1", response.title());
        }

        @Test
        void shouldReturnResponseWithDescription() {
            assertEquals("Lesson description", response.description());
        }

        @Test
        void shouldFindModuleById() {
            verify(moduleRepository, times(1)).findById(1L);
        }

        @Test
        void shouldSaveLessonOnce() {
            verify(lessonRepository, times(1)).save(any(Lesson.class));
        }

        @Test
        void shouldMapSavedLessonToResponse() {
            verify(lessonMapper, times(1)).toResponse(any(Lesson.class));
        }

        @Test
        void shouldSaveLessonWithCorrectTitle() {
            verify(lessonRepository).save(argThat(lesson ->
                    lesson.getTitle().equals("Lesson 1")
            ));
        }

        @Test
        void shouldSaveLessonWithCorrectDescription() {
            verify(lessonRepository).save(argThat(lesson ->
                    lesson.getDescription().equals("Lesson description")
            ));
        }

        @Test
        void shouldSaveLessonWithCorrectModule() {
            verify(lessonRepository).save(argThat(lesson ->
                    lesson.getModule().getId().equals(1L)
            ));
        }
    }

    @Nested
    class CreateLessonWithMissingModule {

        @BeforeEach
        void setup() {
            // Arrange
            mockModuleDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenModuleDoesNotExist() {
            // Arrange
            LessonRequest request = validLessonRequest();

            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () ->
                    lessonService.create(request)
            );
        }

        @Test
        void shouldNotSaveLessonWhenModuleDoesNotExist() {
            // Act
            createLessonIgnoringResourceNotFoundException();

            // Assert
            verify(lessonRepository, never()).save(any(Lesson.class));
        }

        @Test
        void shouldNotMapLessonWhenModuleDoesNotExist() {
            // Act
            createLessonIgnoringResourceNotFoundException();

            // Assert
            verify(lessonMapper, never()).toResponse(any(Lesson.class));
        }
    }

    @Nested
    class UpdateLessonWithValidInput {

        private LessonResponse response;

        @BeforeEach
        void setup() {
            // Arrange
            LessonRequest request = validUpdatedLessonRequest();

            mockLessonExists();
            mockLessonSavedAsUpdated();
            mockUpdatedLessonMappedToResponse();

            // Act
            response = lessonService.update(1L, request);
        }

        @Test
        void shouldReturnLessonResponse() {
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithLessonId() {
            assertEquals(1L, response.lessonId());
        }

        @Test
        void shouldReturnResponseWithUpdatedTitle() {
            assertEquals("Updated Lesson", response.title());
        }

        @Test
        void shouldReturnResponseWithUpdatedDescription() {
            assertEquals("Updated description", response.description());
        }

        @Test
        void shouldFindLessonById() {
            verify(lessonRepository, times(1)).findById(1L);
        }

        @Test
        void shouldSaveLessonOnce() {
            verify(lessonRepository, times(1)).save(any(Lesson.class));
        }

        @Test
        void shouldMapSavedLessonToResponse() {
            verify(lessonMapper, times(1)).toResponse(any(Lesson.class));
        }

        @Test
        void shouldSaveLessonWithUpdatedTitle() {
            verify(lessonRepository).save(argThat(lesson ->
                    lesson.getTitle().equals("Updated Lesson")
            ));
        }

        @Test
        void shouldSaveLessonWithUpdatedDescription() {
            verify(lessonRepository).save(argThat(lesson ->
                    lesson.getDescription().equals("Updated description")
            ));
        }

        @Test
        void shouldKeepSameModule() {
            verify(lessonRepository).save(argThat(lesson ->
                    lesson.getModule().getId().equals(1L)
            ));
        }
    }

    @Nested
    class UpdateLessonWithMissingLesson {

        @BeforeEach
        void setup() {
            // Arrange
            mockLessonDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenLessonDoesNotExist() {
            assertThrows(ResourceNotFoundException.class, () ->
                    lessonService.update(1L, validUpdatedLessonRequest())
            );
        }

        @Test
        void shouldNotSaveLessonWhenLessonDoesNotExist() {
            // Act
            updateLessonIgnoringResourceNotFoundException();

            // Assert
            verify(lessonRepository, never()).save(any(Lesson.class));
        }

        @Test
        void shouldNotMapLessonWhenLessonDoesNotExist() {
            // Act
            updateLessonIgnoringResourceNotFoundException();

            // Assert
            verify(lessonMapper, never()).toResponse(any(Lesson.class));
        }
    }

    @Nested
    class DeleteLessonWithValidInput {

        @BeforeEach
        void setup() {
            // Arrange
            mockLessonExists();

            // Act
            lessonService.delete(1L);
        }

        @Test
        void shouldFindLessonById() {
            verify(lessonRepository, times(1)).findById(1L);
        }

        @Test
        void shouldDeleteLessonOnce() {
            verify(lessonRepository, times(1)).delete(any(Lesson.class));
        }

        @Test
        void shouldDeleteCorrectLesson() {
            verify(lessonRepository).delete(argThat(lesson ->
                    lesson.getId().equals(1L)
            ));
        }
    }

    @Nested
    class DeleteLessonWithMissingLesson {

        @BeforeEach
        void setup() {
            // Arrange
            mockLessonDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenLessonDoesNotExist() {
            assertThrows(ResourceNotFoundException.class, () ->
                    lessonService.delete(1L)
            );
        }

        @Test
        void shouldNotDeleteLessonWhenLessonDoesNotExist() {
            // Act
            deleteLessonIgnoringResourceNotFoundException();

            // Assert
            verify(lessonRepository, never()).delete(any(Lesson.class));
        }
    }

    private void mockModuleExists() {
        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(validModule()));
    }

    private void mockModuleDoesNotExist() {
        when(moduleRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    private void mockLessonExists() {
        when(lessonRepository.findById(1L))
                .thenReturn(Optional.of(savedLesson()));
    }

    private void mockLessonDoesNotExist() {
        when(lessonRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    private void mockLessonSaved() {
        when(lessonRepository.save(any(Lesson.class)))
                .thenReturn(savedLesson());
    }

    private void mockLessonSavedAsUpdated() {
        when(lessonRepository.save(any(Lesson.class)))
                .thenReturn(updatedLesson());
    }

    private void mockLessonMappedToResponse() {
        when(lessonMapper.toResponse(any(Lesson.class)))
                .thenReturn(lessonResponse());
    }

    private void mockUpdatedLessonMappedToResponse() {
        when(lessonMapper.toResponse(any(Lesson.class)))
                .thenReturn(updatedLessonResponse());
    }

    private void createLessonIgnoringResourceNotFoundException() {
        try {
            lessonService.create(validLessonRequest());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private void updateLessonIgnoringResourceNotFoundException() {
        try {
            lessonService.update(1L, validUpdatedLessonRequest());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private void deleteLessonIgnoringResourceNotFoundException() {
        try {
            lessonService.delete(1L);
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private LessonRequest validLessonRequest() {
        return new LessonRequest(
                1L,
                "Lesson 1",
                "Lesson description"
        );
    }

    private LessonRequest validUpdatedLessonRequest() {
        return new LessonRequest(
                1L,
                "Updated Lesson",
                "Updated description"
        );
    }

    private Module validModule() {
        Module module = new Module();

        module.setId(1L);
        module.setName("Module 1");
        module.setDescription("Module description");

        return module;
    }

    private Lesson savedLesson() {
        Lesson lesson = new Lesson();

        lesson.setId(1L);
        lesson.setTitle("Lesson 1");
        lesson.setDescription("Lesson description");
        lesson.setModule(validModule());

        return lesson;
    }

    private Lesson updatedLesson() {
        Lesson lesson = new Lesson();

        lesson.setId(1L);
        lesson.setTitle("Updated Lesson");
        lesson.setDescription("Updated description");
        lesson.setModule(validModule());

        return lesson;
    }

    private LessonResponse lessonResponse() {
        return new LessonResponse(
                1L,
                1L,
                "Lesson 1",
                "Lesson description"
        );
    }

    private LessonResponse updatedLessonResponse() {
        return new LessonResponse(
                1L,
                1L,
                "Updated Lesson",
                "Updated description"
        );
    }
}