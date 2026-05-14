package com.example.backend.integration;

import com.example.backend.entity.Course;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CreateCourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void cleanDatabase() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class CreateCourseWithValidInput {

        private User owner;
        private Course savedCourse;
        private User updatedOwner;
        private int status;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            owner = saveValidUser();

            String jsonBody = validCreateCourseJson(owner.getUserId());

            // Act
            status = mockMvc.perform(post("/api/courses/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            savedCourse = courseRepository.findAll().getFirst();
            updatedOwner = userRepository.findById(owner.getUserId()).orElseThrow();
        }

        @Test
        void shouldReturnCreatedStatus() {
            assertEquals(201, status);
        }

        @Test
        void shouldSaveOneCourse() {
            assertEquals(1, courseRepository.count());
        }

        @Test
        void shouldSaveCourseWithGeneratedId() {
            assertNotNull(savedCourse.getId());
        }

        @Test
        void shouldSaveCourseWithCorrectTitle() {
            assertEquals("Database Fundamentals", savedCourse.getTitle());
        }

        @Test
        void shouldSaveCourseWithCorrectShortDescription() {
            assertEquals("Understand how databases work.", savedCourse.getShortDescription());
        }

        @Test
        void shouldSaveCourseWithCorrectDescription() {
            assertEquals(
                    "Learn about relational databases, tables, primary keys, foreign keys, SQL queries, and basic database design.",
                    savedCourse.getDescription()
            );
        }

        @Test
        void shouldSaveCourseWithCorrectPrice() {
            assertEquals(119.00, savedCourse.getPrice(), 0.001);
        }

        @Test
        void shouldSaveCourseAsUnpublished() {
            assertFalse(savedCourse.isPublished());
        }

        @Test
        void shouldSaveCourseWithLastEditedDate() {
            assertNotNull(savedCourse.getLastEdited());
        }

        @Test
        void shouldSaveCourseWithCorrectOwner() {
            assertEquals(owner.getUserId(), savedCourse.getOwner().getUserId());
        }

        @Test
        void shouldMakeOwnerCourseProvider() {
            assertTrue(updatedOwner.isCourseProvider());
        }
    }

    @Nested
    class CreateCourseWithMissingOwner {

        private int status;
        private long courseCountAfterRequest;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            Long missingOwnerId = 999L;

            String jsonBody = validCreateCourseJson(missingOwnerId);

            // Act
            status = mockMvc.perform(post("/api/courses/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            courseCountAfterRequest = courseRepository.count();
        }

        @Test
        void shouldReturnNotFoundStatus() {
            assertEquals(404, status);
        }

        @Test
        void shouldNotSaveCourse() {
            assertEquals(0, courseCountAfterRequest);
        }
    }

    private User saveValidUser() {
        User user = new User();

        user.setUsername("emil");
        user.setEmail("emil@test.com");
        user.setHashedPassword("hashed-password");
        user.setCourseProvider(false);

        return userRepository.save(user);
    }

    private String validCreateCourseJson(Long ownerId) {
        return """
                {
                    "ownerId": %d,
                    "title": "Database Fundamentals",
                    "shortDescription": "Understand how databases work.",
                    "description": "Learn about relational databases, tables, primary keys, foreign keys, SQL queries, and basic database design.",
                    "price": 119
                }
                """.formatted(ownerId);
    }
}