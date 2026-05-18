package com.example.backend.integration;

import com.example.backend.entity.Course;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.Module;
import com.example.backend.entity.User;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LessonIntegrationTest {

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

    @BeforeEach
    void cleanDatabase() {
        lessonRepository.deleteAll();
        moduleRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class CreateLessonWithValidInput {

        private Module module;
        private Lesson savedLesson;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            module = saveValidModule(course);

            String jsonBody = validLessonJson(module.getId());

            // Act
            status = mockMvc.perform(post("/api/lessons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedLesson = lessonRepository.findAll().getFirst();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveOneLesson() {
            assertEquals(1, lessonRepository.count());
        }

        @Test
        void shouldSaveLessonWithGeneratedId() {
            assertNotNull(savedLesson.getId());
        }

        @Test
        void shouldSaveLessonWithCorrectTitle() {
            assertEquals("Introduction to SQL", savedLesson.getTitle());
        }

        @Test
        void shouldSaveLessonWithCorrectDescription() {
            assertEquals("Learn the basics of SQL queries.", savedLesson.getDescription());
        }

        @Test
        void shouldSaveLessonWithCorrectModule() {
            assertEquals(module.getId(), savedLesson.getModule().getId());
        }

        @Test
        void shouldSaveLessonWithOrderNumberOne() {
            assertEquals(1, savedLesson.getOrderNumber());
        }
    }

    @Nested
    class CreateLessonWhenModuleAlreadyHasLessons {

        private Module module;
        private Lesson savedLesson;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            module = saveValidModule(course);

            saveValidLesson(module, "Lesson 1", "Description 1", 1);
            saveValidLesson(module, "Lesson 2", "Description 2", 2);

            String jsonBody = validLessonJson(module.getId());

            // Act
            status = mockMvc.perform(post("/api/lessons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedLesson = lessonRepository.findAll()
                    .stream()
                    .filter(lesson -> lesson.getTitle().equals("Introduction to SQL"))
                    .findFirst()
                    .orElseThrow();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveThreeLessons() {
            assertEquals(3, lessonRepository.count());
        }

        @Test
        void shouldSaveNewLessonWithNextOrderNumber() {
            assertEquals(3, savedLesson.getOrderNumber());
        }
    }

    @Nested
    class CreateLessonWithMissingModule {

        private int status;
        private long lessonCountAfterRequest;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingModuleId = 999L;

            String jsonBody = validLessonJson(missingModuleId);

            // Act
            status = mockMvc.perform(post("/api/lessons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            lessonCountAfterRequest = lessonRepository.count();
        }

        @Test
        void shouldReturnNotFoundStatus() {
            assertEquals(404, status);
        }

        @Test
        void shouldNotSaveLesson() {
            assertEquals(0, lessonCountAfterRequest);
        }
    }

    @Nested
    class UpdateLessonWithValidInput {

        private Module module;
        private Lesson lesson;
        private Lesson updatedLesson;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            module = saveValidModule(course);
            lesson = saveValidLesson(module, "Old Title", "Old description", 1);

            String jsonBody = updateLessonJson(module.getId());

            // Act
            status = mockMvc.perform(put("/api/lessons/" + lesson.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            updatedLesson = lessonRepository.findById(lesson.getId()).orElseThrow();
        }

        @Test
        void shouldReturnOkStatus() {
            assertEquals(200, status);
        }

        @Test
        void shouldStillHaveOneLesson() {
            assertEquals(1, lessonRepository.count());
        }

        @Test
        void shouldUpdateLessonTitle() {
            assertEquals("Updated Lesson", updatedLesson.getTitle());
        }

        @Test
        void shouldUpdateLessonDescription() {
            assertEquals("Updated lesson description.", updatedLesson.getDescription());
        }

        @Test
        void shouldKeepSameModule() {
            assertEquals(module.getId(), updatedLesson.getModule().getId());
        }

        @Test
        void shouldKeepSameOrderNumber() {
            assertEquals(1, updatedLesson.getOrderNumber());
        }
    }

    @Nested
    class UpdateLessonWithMissingLesson {

        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);

            Long missingLessonId = 999L;

            String jsonBody = updateLessonJson(module.getId());

            // Act
            status = mockMvc.perform(put("/api/lessons/" + missingLessonId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
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
    class UpdateLessonWithMissingModule {

        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            Lesson lesson = saveValidLesson(module, "Old Title", "Old description", 1);

            Long missingModuleId = 999L;

            String jsonBody = updateLessonJson(missingModuleId);

            // Act
            status = mockMvc.perform(put("/api/lessons/" + lesson.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
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
    class DeleteLessonWithValidInput {

        private Module module;
        private Lesson lessonToDelete;
        private int status;
        private long lessonCountAfterDelete;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            module = saveValidModule(course);

            saveValidLesson(module, "Lesson 1", "Description 1", 1);
            lessonToDelete = saveValidLesson(module, "Lesson 2", "Description 2", 2);
            saveValidLesson(module, "Lesson 3", "Description 3", 3);

            // Act
            status = mockMvc.perform(delete("/api/lessons/" + lessonToDelete.getId()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            lessonCountAfterDelete = lessonRepository.count();
        }

        @Test
        void shouldReturnNoContentStatus() {
            assertEquals(204, status);
        }

        @Test
        void shouldDeleteOneLesson() {
            assertEquals(2, lessonCountAfterDelete);
        }

        @Test
        void shouldRemoveDeletedLessonFromDatabase() {
            assertFalse(lessonRepository.existsById(lessonToDelete.getId()));
        }

        @Test
        void shouldKeepFirstLessonOrderNumberAsOne() {
            List<Lesson> lessons = lessonRepository.findAllByModule_ModuleId(module.getId())
                    .stream()
                    .sorted(Comparator.comparing(Lesson::getOrderNumber))
                    .toList();

            assertEquals(1, lessons.getFirst().getOrderNumber());
        }

        @Test
        void shouldShiftLessonsAfterDeletedLessonDownByOne() {
            List<Lesson> lessons = lessonRepository.findAllByModule_ModuleId(module.getId())
                    .stream()
                    .sorted(Comparator.comparing(Lesson::getOrderNumber))
                    .toList();

            assertEquals(2, lessons.get(1).getOrderNumber());
        }

        @Test
        void shouldMoveThirdLessonIntoSecondPosition() {
            List<Lesson> lessons = lessonRepository.findAllByModule_ModuleId(module.getId())
                    .stream()
                    .sorted(Comparator.comparing(Lesson::getOrderNumber))
                    .toList();

            assertEquals("Lesson 3", lessons.get(1).getTitle());
        }
    }

    @Nested
    class DeleteLessonWithMissingLesson {

        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingLessonId = 999L;

            // Act
            status = mockMvc.perform(delete("/api/lessons/" + missingLessonId))
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
    class GetAllLessonsByModuleId {

        private Module module;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            module = saveValidModule(course);

            saveValidLesson(module, "Lesson 1", "Description 1", 1);
            saveValidLesson(module, "Lesson 2", "Description 2", 2);

            // Act
            status = mockMvc.perform(get("/api/lessons/" + module.getId()))
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
    class GetLessonById {

        private Lesson lesson;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            Module module = saveValidModule(course);
            lesson = saveValidLesson(module, "Lesson 1", "Description 1", 1);

            // Act
            status = mockMvc.perform(get("/api/lessons/lesson" + lesson.getId()))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        }

        @Test
        void shouldReturnOkStatus() {
            assertEquals(200, status);
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

    private Lesson saveValidLesson(Module module, String title, String description, int orderNumber) {
        Lesson lesson = new Lesson();

        lesson.setModule(module);
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setOrderNumber(orderNumber);

        return lessonRepository.save(lesson);
    }

    private String validLessonJson(Long moduleId) {
        return """
                {
                    "moduleId": %d,
                    "title": "Introduction to SQL",
                    "description": "Learn the basics of SQL queries."
                }
                """.formatted(moduleId);
    }

    private String updateLessonJson(Long moduleId) {
        return """
                {
                    "moduleId": %d,
                    "title": "Updated Lesson",
                    "description": "Updated lesson description."
                }
                """.formatted(moduleId);
    }
}