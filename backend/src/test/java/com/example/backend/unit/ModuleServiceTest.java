package com.example.backend.unit;

import com.example.backend.business.dto.mapper.ModuleMapper;
import com.example.backend.business.dto.module.ModuleRequest;
import com.example.backend.business.dto.module.ModuleResponse;
import com.example.backend.business.service.ModuleService;
import com.example.backend.entity.Course;
import com.example.backend.entity.Module;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.ModuleRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModuleMapper moduleMapper;

    @InjectMocks
    private ModuleService moduleService;

    @Nested
    class CreateModuleWithValidInput {

        private ModuleResponse response;

        @BeforeEach
        void setup() {
            // Arrange
            ModuleRequest request = validModuleRequest();

            mockCourseExists();
            mockModuleSaved();
            mockModuleMappedToResponse();

            // Act
            response = moduleService.create(request);
        }

        @Test
        void shouldReturnModuleResponse() {
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithGeneratedId() {
            assertEquals(1L, response.moduleId());
        }

        @Test
        void shouldReturnResponseWithCourseId() {
            assertEquals(1L, response.courseId());
        }

        @Test
        void shouldReturnResponseWithName() {
            assertEquals("Module 1", response.name());
        }

        @Test
        void shouldReturnResponseWithDescription() {
            assertEquals("Module description", response.description());
        }

        @Test
        void shouldFindCourseById() {
            verify(courseRepository, times(1)).findById(1L);
        }

        @Test
        void shouldSaveModuleOnce() {
            verify(moduleRepository, times(1)).save(any(Module.class));
        }

        @Test
        void shouldMapSavedModuleToResponse() {
            verify(moduleMapper, times(1)).toResponse(any(Module.class));
        }

        @Test
        void shouldSaveModuleWithCorrectName() {
            verify(moduleRepository).save(argThat(module ->
                    module.getName().equals("Module 1")
            ));
        }

        @Test
        void shouldSaveModuleWithCorrectDescription() {
            verify(moduleRepository).save(argThat(module ->
                    module.getDescription().equals("Module description")
            ));
        }

        @Test
        void shouldSaveModuleWithCorrectCourse() {
            verify(moduleRepository).save(argThat(module ->
                    module.getCourse().getId().equals(1L)
            ));
        }
    }

    @Nested
    class CreateModuleWithMissingCourse {

        @BeforeEach
        void setup() {
            // Arrange
            mockCourseDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenCourseDoesNotExist() {
            // Arrange
            ModuleRequest request = validModuleRequest();

            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () ->
                    moduleService.create(request)
            );
        }

        @Test
        void shouldNotSaveModuleWhenCourseDoesNotExist() {
            // Act
            createModuleIgnoringResourceNotFoundException();

            // Assert
            verify(moduleRepository, never()).save(any(Module.class));
        }

        @Test
        void shouldNotMapModuleWhenCourseDoesNotExist() {
            // Act
            createModuleIgnoringResourceNotFoundException();

            // Assert
            verify(moduleMapper, never()).toResponse(any(Module.class));
        }
    }

    @Nested
    class UpdateModuleWithValidInput {

        private ModuleResponse response;

        @BeforeEach
        void setup() {
            // Arrange
            ModuleRequest request = validUpdatedModuleRequest();

            mockModuleExists();
            mockModuleSavedAsUpdated();
            mockUpdatedModuleMappedToResponse();

            // Act
            response = moduleService.update(1L, request);
        }

        @Test
        void shouldReturnModuleResponse() {
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithModuleId() {
            assertEquals(1L, response.moduleId());
        }

        @Test
        void shouldReturnResponseWithUpdatedName() {
            assertEquals("Updated Module", response.name());
        }

        @Test
        void shouldReturnResponseWithUpdatedDescription() {
            assertEquals("Updated description", response.description());
        }

        @Test
        void shouldFindModuleById() {
            verify(moduleRepository, times(1)).findById(1L);
        }

        @Test
        void shouldSaveModuleOnce() {
            verify(moduleRepository, times(1)).save(any(Module.class));
        }

        @Test
        void shouldMapSavedModuleToResponse() {
            verify(moduleMapper, times(1)).toResponse(any(Module.class));
        }

        @Test
        void shouldSaveModuleWithUpdatedName() {
            verify(moduleRepository).save(argThat(module ->
                    module.getName().equals("Updated Module")
            ));
        }

        @Test
        void shouldSaveModuleWithUpdatedDescription() {
            verify(moduleRepository).save(argThat(module ->
                    module.getDescription().equals("Updated description")
            ));
        }

        @Test
        void shouldKeepSameCourse() {
            verify(moduleRepository).save(argThat(module ->
                    module.getCourse().getId().equals(1L)
            ));
        }
    }

    @Nested
    class UpdateModuleWithMissingModule {

        @BeforeEach
        void setup() {
            // Arrange
            mockModuleDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenModuleDoesNotExist() {
            assertThrows(ResourceNotFoundException.class, () ->
                    moduleService.update(1L, validUpdatedModuleRequest())
            );
        }

        @Test
        void shouldNotSaveModuleWhenModuleDoesNotExist() {
            // Act
            updateModuleIgnoringResourceNotFoundException();

            // Assert
            verify(moduleRepository, never()).save(any(Module.class));
        }

        @Test
        void shouldNotMapModuleWhenModuleDoesNotExist() {
            // Act
            updateModuleIgnoringResourceNotFoundException();

            // Assert
            verify(moduleMapper, never()).toResponse(any(Module.class));
        }
    }

    @Nested
    class DeleteModuleWithValidInput {

        @BeforeEach
        void setup() {
            // Arrange
            mockModuleExists();

            // Act
            moduleService.delete(1L);
        }

        @Test
        void shouldFindModuleById() {
            verify(moduleRepository, times(1)).findById(1L);
        }

        @Test
        void shouldDeleteModuleOnce() {
            verify(moduleRepository, times(1)).delete(any(Module.class));
        }

        @Test
        void shouldDeleteCorrectModule() {
            verify(moduleRepository).delete(argThat(module ->
                    module.getId().equals(1L)
            ));
        }
    }

    @Nested
    class DeleteModuleWithMissingModule {

        @BeforeEach
        void setup() {
            // Arrange
            mockModuleDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenModuleDoesNotExist() {
            assertThrows(ResourceNotFoundException.class, () ->
                    moduleService.delete(1L)
            );
        }

        @Test
        void shouldNotDeleteModuleWhenModuleDoesNotExist() {
            // Act
            deleteModuleIgnoringResourceNotFoundException();

            // Assert
            verify(moduleRepository, never()).delete(any(Module.class));
        }
    }

    private void mockCourseExists() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(validCourse()));
    }

    private void mockCourseDoesNotExist() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    private void mockModuleExists() {
        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(savedModule()));
    }

    private void mockModuleDoesNotExist() {
        when(moduleRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    private void mockModuleSaved() {
        when(moduleRepository.save(any(Module.class)))
                .thenReturn(savedModule());
    }

    private void mockModuleSavedAsUpdated() {
        when(moduleRepository.save(any(Module.class)))
                .thenReturn(updatedModule());
    }

    private void mockModuleMappedToResponse() {
        when(moduleMapper.toResponse(any(Module.class)))
                .thenReturn(moduleResponse());
    }

    private void mockUpdatedModuleMappedToResponse() {
        when(moduleMapper.toResponse(any(Module.class)))
                .thenReturn(updatedModuleResponse());
    }

    private void createModuleIgnoringResourceNotFoundException() {
        try {
            moduleService.create(validModuleRequest());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private void updateModuleIgnoringResourceNotFoundException() {
        try {
            moduleService.update(1L, validUpdatedModuleRequest());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private void deleteModuleIgnoringResourceNotFoundException() {
        try {
            moduleService.delete(1L);
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private ModuleRequest validModuleRequest() {
        return new ModuleRequest(
                1L,
                "Module 1",
                "Module description"
        );
    }

    private ModuleRequest validUpdatedModuleRequest() {
        return new ModuleRequest(
                1L,
                "Updated Module",
                "Updated description"
        );
    }

    private Course validCourse() {
        Course course = new Course();

        course.setId(1L);
        course.setTitle("Test course");

        return course;
    }

    private Module savedModule() {
        Module module = new Module();

        module.setId(1L);
        module.setName("Module 1");
        module.setDescription("Module description");
        module.setCourse(validCourse());

        return module;
    }

    private Module updatedModule() {
        Module module = new Module();

        module.setId(1L);
        module.setName("Updated Module");
        module.setDescription("Updated description");
        module.setCourse(validCourse());

        return module;
    }

    private ModuleResponse moduleResponse() {
        return new ModuleResponse(
                1L,
                1L,
                "Module 1",
                "Module description",
                1,
                new ArrayList<>()
        );
    }

    private ModuleResponse updatedModuleResponse() {
        return new ModuleResponse(
                1L,
                1L,
                "Updated Module",
                "Updated description",
                1,
                new ArrayList<>()
        );
    }
}