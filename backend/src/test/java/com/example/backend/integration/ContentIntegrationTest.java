package com.example.backend.integration;

import com.example.backend.business.dto.content.ContentType;
import com.example.backend.entity.Content;
import com.example.backend.entity.Course;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.Module;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.ContentRepository;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.LessonRepository;
import com.example.backend.persistence.repository.ModuleRepository;
import com.example.backend.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ContentRepository contentRepository;

    @BeforeEach
    void cleanDatabase() {
        contentRepository.deleteAll();
        lessonRepository.deleteAll();
        moduleRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class UploadMarkdownContentWithValidInput {

        private Lesson lesson;
        private Content savedContent;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            lesson = saveValidLesson(module);

            MockMultipartFile file = markdownFile();

            // Act
            status = mockMvc.perform(multipart("/api/contents")
                            .file(file)
                            .param("lessonId", lesson.getId().toString()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedContent = contentRepository.findAll().getFirst();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveOneContent() {
            assertEquals(1, contentRepository.count());
        }

        @Test
        void shouldSaveContentWithGeneratedId() {
            assertNotNull(savedContent.getContentId());
        }

        @Test
        void shouldSaveContentWithCorrectLesson() {
            assertEquals(lesson.getId(), savedContent.getLesson().getId());
        }

        @Test
        void shouldSaveOriginalFileName() {
            assertEquals("lesson-notes.md", savedContent.getOriginalFileName());
        }

        @Test
        void shouldSaveMarkdownContentType() {
            assertEquals(ContentType.MARKDOWN, savedContent.getContentType());
        }

        @Test
        void shouldSaveContentWithOrderNumberOne() {
            assertEquals(1, savedContent.getOrderNumber());
        }

        @Test
        void shouldSaveFilePath() {
            assertNotNull(savedContent.getFilePath());
        }

        @Test
        void shouldCreatePhysicalFile() {
            assertTrue(Files.exists(Path.of(savedContent.getFilePath())));
        }
    }

    @Nested
    class UploadImageContentWithValidInput {

        private Content savedContent;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            Lesson lesson = saveValidLesson(module);

            MockMultipartFile file = imageFile();

            // Act
            status = mockMvc.perform(multipart("/api/contents")
                            .file(file)
                            .param("lessonId", lesson.getId().toString()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedContent = contentRepository.findAll().getFirst();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveImageContentType() {
            assertEquals(ContentType.IMAGE, savedContent.getContentType());
        }

        @Test
        void shouldSaveOriginalFileName() {
            assertEquals("diagram.png", savedContent.getOriginalFileName());
        }
    }

    @Nested
    class UploadVideoContentWithValidInput {

        private Content savedContent;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            Lesson lesson = saveValidLesson(module);

            MockMultipartFile file = videoFile();

            // Act
            status = mockMvc.perform(multipart("/api/contents")
                            .file(file)
                            .param("lessonId", lesson.getId().toString()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedContent = contentRepository.findAll().getFirst();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveVideoContentType() {
            assertEquals(ContentType.VIDEO, savedContent.getContentType());
        }

        @Test
        void shouldSaveOriginalFileName() {
            assertEquals("intro-video.mp4", savedContent.getOriginalFileName());
        }
    }

    @Nested
    class UploadContentWhenLessonAlreadyHasContent {

        private Lesson lesson;
        private Content savedContent;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            lesson = saveValidLesson(module);

            saveValidContent(lesson, "first.md", ContentType.MARKDOWN, 1);
            saveValidContent(lesson, "second.md", ContentType.MARKDOWN, 2);

            MockMultipartFile file = markdownFile();

            // Act
            status = mockMvc.perform(multipart("/api/contents")
                            .file(file)
                            .param("lessonId", lesson.getId().toString()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedContent = contentRepository.findAll()
                    .stream()
                    .filter(content -> content.getOriginalFileName().equals("lesson-notes.md"))
                    .findFirst()
                    .orElseThrow();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveThreeContents() {
            assertEquals(3, contentRepository.count());
        }

        @Test
        void shouldSaveNewContentWithNextOrderNumber() {
            assertEquals(3, savedContent.getOrderNumber());
        }
    }

    @Nested
    class UploadContentWithMissingLesson {

        private int status;
        private long contentCountAfterRequest;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingLessonId = 999L;

            MockMultipartFile file = markdownFile();

            // Act
            status = mockMvc.perform(multipart("/api/contents")
                            .file(file)
                            .param("lessonId", missingLessonId.toString()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            contentCountAfterRequest = contentRepository.count();
        }

        @Test
        void shouldReturnNotFoundStatus() {
            assertEquals(404, status);
        }

        @Test
        void shouldNotSaveContent() {
            assertEquals(0, contentCountAfterRequest);
        }
    }

    @Nested
    class UploadEmptyFile {

        private int status;
        private long contentCountAfterRequest;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            Lesson lesson = saveValidLesson(module);

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "empty.md",
                    "text/markdown",
                    new byte[0]
            );

            // Act
            status = mockMvc.perform(multipart("/api/contents")
                            .file(file)
                            .param("lessonId", lesson.getId().toString()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            contentCountAfterRequest = contentRepository.count();
        }

        @Test
        void shouldReturnBadRequestStatus() {
            assertEquals(400, status);
        }

        @Test
        void shouldNotSaveContent() {
            assertEquals(0, contentCountAfterRequest);
        }
    }

    @Nested
    class UploadUnsupportedFileType {

        private int status;
        private long contentCountAfterRequest;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            Lesson lesson = saveValidLesson(module);

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "data.pdf",
                    "application/pdf",
                    "fake pdf content".getBytes()
            );

            // Act
            status = mockMvc.perform(multipart("/api/contents")
                            .file(file)
                            .param("lessonId", lesson.getId().toString()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            contentCountAfterRequest = contentRepository.count();
        }

        @Test
        void shouldReturnBadRequestStatus() {
            assertEquals(400, status);
        }

        @Test
        void shouldNotSaveContent() {
            assertEquals(0, contentCountAfterRequest);
        }
    }

    @Nested
    class GetContentById {

        private Content content;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            Lesson lesson = saveValidLesson(module);
            content = saveValidContent(lesson, "lesson-notes.md", ContentType.MARKDOWN, 1);

            // Act
            status = mockMvc.perform(get("/api/contents/" + content.getContentId()))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        }

        @Test
        void shouldReturnOkStatus() {
            assertEquals(200, status);
        }
    }

    @Nested
    class GetContentByMissingId {

        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingContentId = 999L;

            // Act
            status = mockMvc.perform(get("/api/contents/" + missingContentId))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        }

        @Test
        void shouldReturnNotFoundStatus() {
            assertEquals(404, status);
        }
    }

    @Nested
    class GetAllContentsByLessonId {

        private Lesson lesson;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            lesson = saveValidLesson(module);

            saveValidContent(lesson, "first.md", ContentType.MARKDOWN, 1);
            saveValidContent(lesson, "second.md", ContentType.MARKDOWN, 2);

            // Act
            status = mockMvc.perform(get("/api/contents/lesson/" + lesson.getId()))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        }

        @Test
        void shouldReturnOkStatus() {
            assertEquals(200, status);
        }
    }

    @Nested
    class GetFileWithValidInput {

        private Content content;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            Lesson lesson = saveValidLesson(module);

            content = saveValidContentWithPhysicalFile(
                    lesson,
                    "lesson-notes.md",
                    ContentType.MARKDOWN,
                    1,
                    "This is markdown content"
            );

            // Act
            status = mockMvc.perform(get("/api/contents/" + content.getContentId() + "/file"))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        }

        @Test
        void shouldReturnOkStatus() {
            assertEquals(200, status);
        }
    }

    @Nested
    class DeleteContentWithValidInput {

        private Lesson lesson;
        private Content contentToDelete;
        private Path deletedFilePath;
        private int status;
        private long contentCountAfterDelete;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            lesson = saveValidLesson(module);

            saveValidContentWithPhysicalFile(lesson, "first.md", ContentType.MARKDOWN, 1, "first");
            contentToDelete = saveValidContentWithPhysicalFile(lesson, "second.md", ContentType.MARKDOWN, 2, "second");
            saveValidContentWithPhysicalFile(lesson, "third.md", ContentType.MARKDOWN, 3, "third");

            deletedFilePath = Path.of(contentToDelete.getFilePath());

            // Act
            status = mockMvc.perform(delete("/api/contents/" + contentToDelete.getContentId()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            contentCountAfterDelete = contentRepository.count();
        }

        @Test
        void shouldReturnNoContentStatus() {
            assertEquals(204, status);
        }

        @Test
        void shouldDeleteOneContent() {
            assertEquals(2, contentCountAfterDelete);
        }

        @Test
        void shouldRemoveDeletedContentFromDatabase() {
            assertFalse(contentRepository.existsById(contentToDelete.getContentId()));
        }

        @Test
        void shouldDeletePhysicalFile() {
            assertFalse(Files.exists(deletedFilePath));
        }

        @Test
        void shouldKeepFirstContentOrderNumberAsOne() {
            List<Content> contents = contentRepository.findAllByLesson_LessonIdOrderByOrderNumberAsc(lesson.getId());

            assertEquals(1, contents.getFirst().getOrderNumber());
        }

        @Test
        void shouldShiftContentsAfterDeletedContentDownByOne() {
            List<Content> contents = contentRepository.findAllByLesson_LessonIdOrderByOrderNumberAsc(lesson.getId());

            assertEquals(2, contents.get(1).getOrderNumber());
        }

        @Test
        void shouldMoveThirdContentIntoSecondPosition() {
            List<Content> contents = contentRepository.findAllByLesson_LessonIdOrderByOrderNumberAsc(lesson.getId());

            assertEquals("third.md", contents.get(1).getOriginalFileName());
        }
    }

    @Nested
    class DeleteContentWithMissingContent {

        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingContentId = 999L;

            // Act
            status = mockMvc.perform(delete("/api/contents/" + missingContentId))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        }

        @Test
        void shouldReturnNotFoundStatus() {
            assertEquals(404, status);
        }
    }

    private User saveValidUser() {
        User user = new User();

        user.setUsername("emil");
        user.setEmail("emil@test.com");
        user.setHashedPassword("hashed-password");
        user.setCourseProvider(true);

        return userRepository.save(user);
    }

    private Course saveValidCourse(User owner) {
        Course course = new Course();

        course.setOwner(owner);
        course.setTitle("Database Fundamentals");
        course.setShortDescription("Understand how databases work.");
        course.setDescription("Learn about relational databases, tables, primary keys, foreign keys, SQL queries, and basic database design.");
        course.setPrice(119.00);
        course.setPublished(false);
        course.setLastEditedToNow();

        return courseRepository.save(course);
    }

    private Module saveValidModule(Course course) {
        Module module = new Module();

        module.setCourse(course);
        module.setName("SQL Basics");
        module.setDescription("Learn the basics of relational databases.");
        module.setOrderNumber(1);

        return moduleRepository.save(module);
    }

    private Lesson saveValidLesson(Module module) {
        Lesson lesson = new Lesson();

        lesson.setModule(module);
        lesson.setTitle("Introduction to SQL");
        lesson.setDescription("Learn the basics of SQL queries.");
        lesson.setOrderNumber(1);

        return lessonRepository.save(lesson);
    }

    private Content saveValidContent(
            Lesson lesson,
            String originalFileName,
            ContentType contentType,
            int orderNumber
    ) {
        Content content = new Content();

        content.setLesson(lesson);
        content.setOriginalFileName(originalFileName);
        content.setFilePath("data/test-content/" + originalFileName);
        content.setContentType(contentType);
        content.setOrderNumber(orderNumber);

        return contentRepository.save(content);
    }

    private Content saveValidContentWithPhysicalFile(
            Lesson lesson,
            String originalFileName,
            ContentType contentType,
            int orderNumber,
            String fileContent
    ) throws Exception {
        Path directory = Path.of("data", "test-content", "lesson-" + lesson.getId());
        Files.createDirectories(directory);

        Path filePath = directory.resolve(originalFileName);
        Files.writeString(filePath, fileContent);

        Content content = new Content();

        content.setLesson(lesson);
        content.setOriginalFileName(originalFileName);
        content.setFilePath(filePath.toString());
        content.setContentType(contentType);
        content.setOrderNumber(orderNumber);

        return contentRepository.save(content);
    }

    private MockMultipartFile markdownFile() {
        return new MockMultipartFile(
                "file",
                "lesson-notes.md",
                "text/markdown",
                "# Lesson notes".getBytes()
        );
    }

    private MockMultipartFile imageFile() {
        return new MockMultipartFile(
                "file",
                "diagram.png",
                "image/png",
                "fake image content".getBytes()
        );
    }

    private MockMultipartFile videoFile() {
        return new MockMultipartFile(
                "file",
                "intro-video.mp4",
                "video/mp4",
                "fake video content".getBytes()
        );
    }
}