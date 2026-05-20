package com.example.backend.unit;

import com.example.backend.business.dto.content.ContentResponse;
import com.example.backend.business.dto.content.ContentType;
import com.example.backend.business.dto.mapper.ContentMapper;
import com.example.backend.business.service.ContentService;
import com.example.backend.entity.Content;
import com.example.backend.entity.Lesson;
import com.example.backend.persistence.repository.ContentRepository;
import com.example.backend.persistence.repository.LessonRepository;
import com.example.backend.shared.exception.EmptyFileException;
import com.example.backend.shared.exception.InvalidFileException;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ContentMapper contentMapper;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ContentService contentService;

    @Nested
    class UploadContentWithValidImageFile {

        private ContentResponse response;

        @BeforeEach
        void setup() throws IOException {
            // Arrange
            mockLessonExists();
            mockValidImageFile();
            mockHighestOrderNumberIsTwo();
            mockContentSaved();
            mockContentMappedToResponse();

            // Act
            response = contentService.upload(1L, file);
        }

        @Test
        void shouldReturnContentResponse() {
            assertNotNull(response);
        }

        @Test
        void shouldReturnResponseWithGeneratedId() {
            assertEquals(1L, response.contentId());
        }

        @Test
        void shouldReturnResponseWithLessonId() {
            assertEquals(1L, response.lessonId());
        }

        @Test
        void shouldReturnResponseWithOriginalFileName() {
            assertEquals("logo.png", response.originalFileName());
        }

        @Test
        void shouldReturnResponseWithContentType() {
            assertEquals("IMAGE", response.contentType());
        }

        @Test
        void shouldReturnResponseWithOrderNumber() {
            assertEquals(3, response.orderNumber());
        }

        @Test
        void shouldFindLessonById() {
            verify(lessonRepository, times(1)).findById(1L);
        }

        @Test
        void shouldFindHighestOrderNumberForLesson() {
            verify(contentRepository, times(1)).findHighestOrderNumberByLessonId(1L);
        }

        @Test
        void shouldTransferFileOnce() throws IOException {
            verify(file, times(1)).transferTo(any(Path.class));
        }

        @Test
        void shouldSaveContentOnce() {
            verify(contentRepository, times(1)).save(any(Content.class));
        }

        @Test
        void shouldMapSavedContentToResponse() {
            verify(contentMapper, times(1)).toResponse(any(Content.class));
        }

        @Test
        void shouldSaveContentWithCorrectLesson() {
            verify(contentRepository).save(argThat(content ->
                    content.getLesson().getLessonId().equals(1L)
            ));
        }

        @Test
        void shouldSaveContentWithCorrectOriginalFileName() {
            verify(contentRepository).save(argThat(content ->
                    content.getOriginalFileName().equals("logo.png")
            ));
        }

        @Test
        void shouldSaveContentWithImageType() {
            verify(contentRepository).save(argThat(content ->
                    content.getContentType() == ContentType.IMAGE
            ));
        }

        @Test
        void shouldSaveContentWithNextOrderNumber() {
            verify(contentRepository).save(argThat(content ->
                    content.getOrderNumber() == 3
            ));
        }

        @Test
        void shouldSaveContentWithFilePathContainingLessonFolder() {
            verify(contentRepository).save(argThat(content ->
                    content.getFilePath().contains("lesson-1")
            ));
        }

        @Test
        void shouldSaveContentWithFilePathContainingOriginalFileName() {
            verify(contentRepository).save(argThat(content ->
                    content.getFilePath().contains("logo.png")
            ));
        }
    }

    @Nested
    class UploadContentWithMissingLesson {

        @BeforeEach
        void setup() {
            // Arrange
            mockLessonDoesNotExist();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenLessonDoesNotExist() {
            assertThrows(ResourceNotFoundException.class, () ->
                    contentService.upload(1L, file)
            );
        }

        @Test
        void shouldNotSaveContentWhenLessonDoesNotExist() {
            // Act
            uploadIgnoringResourceNotFoundException();

            // Assert
            verify(contentRepository, never()).save(any(Content.class));
        }

        @Test
        void shouldNotTransferFileWhenLessonDoesNotExist() throws IOException {
            // Act
            uploadIgnoringResourceNotFoundException();

            // Assert
            verify(file, never()).transferTo(any(Path.class));
        }

        @Test
        void shouldNotMapContentWhenLessonDoesNotExist() {
            // Act
            uploadIgnoringResourceNotFoundException();

            // Assert
            verify(contentMapper, never()).toResponse(any(Content.class));
        }
    }

    @Nested
    class UploadContentWithEmptyFile {

        @BeforeEach
        void setup() {
            // Arrange
            mockLessonExists();
            mockEmptyFile();
        }

        @Test
        void shouldThrowEmptyFileExceptionWhenFileIsEmpty() {
            assertThrows(EmptyFileException.class, () ->
                    contentService.upload(1L, file)
            );
        }

        @Test
        void shouldNotSaveContentWhenFileIsEmpty() {
            // Act
            uploadIgnoringEmptyFileException();

            // Assert
            verify(contentRepository, never()).save(any(Content.class));
        }

        @Test
        void shouldNotTransferFileWhenFileIsEmpty() throws IOException {
            // Act
            uploadIgnoringEmptyFileException();

            // Assert
            verify(file, never()).transferTo(any(Path.class));
        }

        @Test
        void shouldNotMapContentWhenFileIsEmpty() {
            // Act
            uploadIgnoringEmptyFileException();

            // Assert
            verify(contentMapper, never()).toResponse(any(Content.class));
        }
    }

    @Nested
    class UploadContentWithUnsupportedFileType {

        @BeforeEach
        void setup() {
            // Arrange
            mockLessonExists();
            mockUnsupportedFileType();
        }

        @Test
        void shouldThrowInvalidFileExceptionWhenFileTypeIsUnsupported() {
            assertThrows(InvalidFileException.class, () ->
                    contentService.upload(1L, file)
            );
        }

        @Test
        void shouldNotSaveContentWhenFileTypeIsUnsupported() {
            // Act
            uploadIgnoringInvalidFileException();

            // Assert
            verify(contentRepository, never()).save(any(Content.class));
        }

        @Test
        void shouldNotTransferFileWhenFileTypeIsUnsupported() throws IOException {
            // Act
            uploadIgnoringInvalidFileException();

            // Assert
            verify(file, never()).transferTo(any(Path.class));
        }

        @Test
        void shouldNotMapContentWhenFileTypeIsUnsupported() {
            // Act
            uploadIgnoringInvalidFileException();

            // Assert
            verify(contentMapper, never()).toResponse(any(Content.class));
        }
    }

    @Nested
    class UploadContentWhenFileTransferFails {

        @BeforeEach
        void setup() throws IOException {
            // Arrange
            mockLessonExists();
            mockValidImageFile();
            mockFileTransferFails();
        }

        @Test
        void shouldThrowInvalidFileExceptionWhenFileTransferFails() {
            assertThrows(InvalidFileException.class, () ->
                    contentService.upload(1L, file)
            );
        }

        @Test
        void shouldNotSaveContentWhenFileTransferFails() {
            // Act
            uploadIgnoringInvalidFileException();

            // Assert
            verify(contentRepository, never()).save(any(Content.class));
        }

        @Test
        void shouldNotMapContentWhenFileTransferFails() {
            // Act
            uploadIgnoringInvalidFileException();

            // Assert
            verify(contentMapper, never()).toResponse(any(Content.class));
        }
    }

    private void mockLessonExists() {
        when(lessonRepository.findById(1L))
                .thenReturn(Optional.of(validLesson()));
    }

    private void mockLessonDoesNotExist() {
        when(lessonRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    private void mockValidImageFile() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getOriginalFilename()).thenReturn("logo.png");
    }

    private void mockEmptyFile() {
        when(file.isEmpty()).thenReturn(true);
    }

    private void mockUnsupportedFileType() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
    }

    private void mockHighestOrderNumberIsTwo() {
        when(contentRepository.findHighestOrderNumberByLessonId(1L))
                .thenReturn(2);
    }

    private void mockContentSaved() {
        when(contentRepository.save(any(Content.class)))
                .thenReturn(savedContent());
    }

    private void mockContentMappedToResponse() {
        when(contentMapper.toResponse(any(Content.class)))
                .thenReturn(contentResponse());
    }

    private void mockFileTransferFails() throws IOException {
        doThrow(new IOException("Could not transfer file"))
                .when(file)
                .transferTo(any(Path.class));
    }

    private void uploadIgnoringResourceNotFoundException() {
        try {
            contentService.upload(1L, file);
        } catch (ResourceNotFoundException ignored) {
        }
    }

    private void uploadIgnoringEmptyFileException() {
        try {
            contentService.upload(1L, file);
        } catch (EmptyFileException ignored) {
        }
    }

    private void uploadIgnoringInvalidFileException() {
        try {
            contentService.upload(1L, file);
        } catch (InvalidFileException ignored) {
        }
    }

    private Lesson validLesson() {
        Lesson lesson = new Lesson();

        lesson.setLessonId(1L);
        lesson.setTitle("Lesson 1");
        lesson.setDescription("Lesson description");

        return lesson;
    }

    private Content savedContent() {
        Content content = new Content();

        content.setContentId(1L);
        content.setLesson(validLesson());
        content.setOriginalFileName("logo.png");
        content.setFilePath("seedData/lesson-content/lesson-1/test-logo.png");
        content.setContentType(ContentType.IMAGE);
        content.setOrderNumber(3);

        return content;
    }

    private ContentResponse contentResponse() {
        return new ContentResponse(
                1L,
                1L,
                "logo.png",
                "IMAGE",
                3,
                "/api/contents/1/file"
        );
    }
}