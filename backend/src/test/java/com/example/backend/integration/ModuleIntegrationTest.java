package com.example.backend.integration;

import com.example.backend.entity.Course;
import com.example.backend.entity.Module;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
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
class ModuleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @BeforeEach
    void cleanDatabase() {
        moduleRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class CreateModuleWithValidInput {

        private Course course;
        private Module savedModule;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            course = saveValidCourse(owner);

            String jsonBody = validModuleJson(course.getId());

            // Act
            status = mockMvc.perform(post("/api/modules")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedModule = moduleRepository.findAll().getFirst();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveOneModule() {
            assertEquals(1, moduleRepository.count());
        }

        @Test
        void shouldSaveModuleWithGeneratedId() {
            assertNotNull(savedModule.getId());
        }

        @Test
        void shouldSaveModuleWithCorrectName() {
            assertEquals("Getting Started", savedModule.getName());
        }

        @Test
        void shouldSaveModuleWithCorrectDescription() {
            assertEquals("Learn the basic concepts before starting the course.", savedModule.getDescription());
        }

        @Test
        void shouldSaveModuleWithCorrectCourse() {
            assertEquals(course.getId(), savedModule.getCourse().getId());
        }

        @Test
        void shouldSaveModuleWithOrderNumberOne() {
            assertEquals(1, savedModule.getOrderNumber());
        }
    }

    @Nested
    class CreateModuleWhenCourseAlreadyHasModules {

        private Course course;
        private Module savedModule;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            course = saveValidCourse(owner);

            saveValidModule(course, "First Module", "First description", 1);
            saveValidModule(course, "Second Module", "Second description", 2);

            String jsonBody = validModuleJson(course.getId());

            // Act
            status = mockMvc.perform(post("/api/modules")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedModule = moduleRepository.findAll()
                    .stream()
                    .filter(module -> module.getName().equals("Getting Started"))
                    .findFirst()
                    .orElseThrow();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveThreeModules() {
            assertEquals(3, moduleRepository.count());
        }

        @Test
        void shouldSaveNewModuleWithNextOrderNumber() {
            assertEquals(3, savedModule.getOrderNumber());
        }
    }

    @Nested
    class CreateModuleWithMissingCourse {

        private int status;
        private long moduleCountAfterRequest;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingCourseId = 999L;

            String jsonBody = validModuleJson(missingCourseId);

            // Act
            status = mockMvc.perform(post("/api/modules")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            moduleCountAfterRequest = moduleRepository.count();
        }

        @Test
        void shouldReturnNotFoundStatus() {
            assertEquals(404, status);
        }

        @Test
        void shouldNotSaveModule() {
            assertEquals(0, moduleCountAfterRequest);
        }
    }

    @Nested
    class UpdateModuleWithValidInput {

        private Course course;
        private Module module;
        private Module updatedModule;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            course = saveValidCourse(owner);
            module = saveValidModule(course, "Old Name", "Old description", 1);

            String jsonBody = updateModuleJson(course.getId());

            // Act
            status = mockMvc.perform(put("/api/modules/" + module.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            updatedModule = moduleRepository.findById(module.getId()).orElseThrow();
        }

        @Test
        void shouldReturnOkStatus() {
            assertEquals(200, status);
        }

        @Test
        void shouldStillHaveOneModule() {
            assertEquals(1, moduleRepository.count());
        }

        @Test
        void shouldUpdateModuleName() {
            assertEquals("Updated Module", updatedModule.getName());
        }

        @Test
        void shouldUpdateModuleDescription() {
            assertEquals("Updated module description.", updatedModule.getDescription());
        }

        @Test
        void shouldKeepSameCourse() {
            assertEquals(course.getId(), updatedModule.getCourse().getId());
        }

        @Test
        void shouldKeepSameOrderNumber() {
            assertEquals(1, updatedModule.getOrderNumber());
        }
    }

    @Nested
    class UpdateModuleWithMissingModule {

        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);

            Long missingModuleId = 999L;

            String jsonBody = updateModuleJson(course.getId());

            // Act
            status = mockMvc.perform(put("/api/modules/" + missingModuleId)
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
    class DeleteModuleWithValidInput {

        private Course course;
        private Module moduleToDelete;
        private int status;
        private long moduleCountAfterDelete;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            course = saveValidCourse(owner);

            saveValidModule(course, "Module 1", "Description 1", 1);
            moduleToDelete = saveValidModule(course, "Module 2", "Description 2", 2);
            saveValidModule(course, "Module 3", "Description 3", 3);

            // Act
            status = mockMvc.perform(delete("/api/modules/" + moduleToDelete.getId()))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            moduleCountAfterDelete = moduleRepository.count();
        }

        @Test
        void shouldReturnNoContentStatus() {
            assertEquals(204, status);
        }

        @Test
        void shouldDeleteOneModule() {
            assertEquals(2, moduleCountAfterDelete);
        }

        @Test
        void shouldRemoveDeletedModuleFromDatabase() {
            assertFalse(moduleRepository.existsById(moduleToDelete.getId()));
        }

        @Test
        void shouldShiftModulesAfterDeletedModuleDownByOne() {
            List<Module> modules = moduleRepository.findAllByCourse_Id(course.getId())
                    .stream()
                    .sorted(Comparator.comparing(Module::getOrderNumber))
                    .toList();

            assertEquals(2, modules.get(1).getOrderNumber());
        }

        @Test
        void shouldKeepFirstModuleOrderNumberAsOne() {
            List<Module> modules = moduleRepository.findAllByCourse_Id(course.getId())
                    .stream()
                    .sorted(Comparator.comparing(Module::getOrderNumber))
                    .toList();

            assertEquals(1, modules.getFirst().getOrderNumber());
        }

        @Test
        void shouldMoveThirdModuleIntoSecondPosition() {
            List<Module> modules = moduleRepository.findAllByCourse_Id(course.getId())
                    .stream()
                    .sorted(Comparator.comparing(Module::getOrderNumber))
                    .toList();

            assertEquals("Module 3", modules.get(1).getName());
        }
    }

    @Nested
    class DeleteModuleWithMissingModule {

        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingModuleId = 999L;

            // Act
            status = mockMvc.perform(delete("/api/modules/" + missingModuleId))
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
    class GetAllModulesByCourseId {

        private Course course;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            course = saveValidCourse(owner);

            saveValidModule(course, "Module 1", "Description 1", 1);
            saveValidModule(course, "Module 2", "Description 2", 2);

            // Act
            status = mockMvc.perform(get("/api/modules/" + course.getId()))
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
    class GetModuleById {

        private Module module;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            User owner = saveValidUser();
            Course course = saveValidCourse(owner);
            module = saveValidModule(course, "Module 1", "Description 1", 1);

            // Act
            status = mockMvc.perform(get("/api/modules/module/" + module.getId()))
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

    private Module saveValidModule(Course course, String name, String description, int orderNumber) {
        Module module = new Module();

        module.setCourse(course);
        module.setName(name);
        module.setDescription(description);
        module.setOrderNumber(orderNumber);

        return moduleRepository.save(module);
    }

    private String validModuleJson(Long courseId) {
        return """
                {
                    "courseId": %d,
                    "name": "Getting Started",
                    "description": "Learn the basic concepts before starting the course."
                }
                """.formatted(courseId);
    }

    private String updateModuleJson(Long courseId) {
        return """
                {
                    "courseId": %d,
                    "name": "Updated Module",
                    "description": "Updated module description."
                }
                """.formatted(courseId);
    }
}