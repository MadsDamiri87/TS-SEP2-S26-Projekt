package com.example.backend.integration;

import com.example.backend.entity.Course;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.EnrollmentRepository;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EnrollmentIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @BeforeEach
  void cleanDatabase() {
    enrollmentRepository.deleteAll();
    courseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Nested
  class EnrollUserInCourseWithValidInput {

    private User user;
    private Course course;
    private int enrollmentStatus;
    private int getCoursesStatus;
    private User updatedUser;
    private String getCoursesResponseBody;

    @BeforeEach
    void setup() throws Exception {
      user = saveValidUser("magnus_mads", "test@course.com");
      User owner = saveValidUser("course_owner", "owner@course.com");
      course = saveValidCourse(owner);

      String enrollmentJson = validEnrollmentJson(user.getUserId(), course.getId());

      enrollmentStatus = mockMvc.perform(post("/api/enrollments")
              .contentType(MediaType.APPLICATION_JSON)
              .content(enrollmentJson))
          .andReturn()
          .getResponse()
          .getStatus();

      updatedUser = userRepository.findById(user.getUserId()).orElseThrow();

      var getResult = mockMvc.perform(get("/api/enrollments/" + user.getUserId()))
          .andReturn()
          .getResponse();

      getCoursesStatus = getResult.getStatus();
      getCoursesResponseBody = getResult.getContentAsString();
    }

    @Test
    void shouldReturnCreatedStatusOnEnrollment() {
      assertEquals(201, enrollmentStatus);
    }

    @Test
    void shouldSaveEnrollmentInDatabase() {
      assertEquals(1, enrollmentRepository.count());
    }

    @Test
    void shouldMakeUserCourseParticipant() {
      assertTrue(updatedUser.isCourseParticipant());
    }

    @Test
    void shouldReturnOkStatusOnGetEnrollments() {
      assertEquals(200, getCoursesStatus);
    }

    @Test
    void shouldContainEnrolledCourseInLibrary() {
      assertTrue(getCoursesResponseBody.contains(course.getTitle()));
      assertTrue(getCoursesResponseBody.contains(course.getShortDescription()));
    }
  }

  @Nested
  class EnrollUserInCourseWithMissingUser {

    private int status;
    private long enrollmentCountAfterRequest;

    @BeforeEach
    void setup() throws Exception {
      User owner = saveValidUser("course_owner", "owner@course.com");
      Course course = saveValidCourse(owner);
      Long missingUserId = 999L;
      String enrollmentJson = validEnrollmentJson(missingUserId, course.getId());

      status = mockMvc.perform(post("/api/enrollments")
              .contentType(MediaType.APPLICATION_JSON)
              .content(enrollmentJson))
          .andReturn()
          .getResponse()
          .getStatus();

      enrollmentCountAfterRequest = enrollmentRepository.count();
    }

    @Test
    void shouldReturnNotFoundStatus() {
      assertEquals(404, status);
    }

    @Test
    void shouldNotSaveEnrollment() {
      assertEquals(0, enrollmentCountAfterRequest);
    }
  }

  @Nested
  class EnrollUserInCourseWithMissingCourse {

    private int status;
    private long enrollmentCountAfterRequest;

    @BeforeEach
    void setup() throws Exception {
      User user = saveValidUser("magnus_mads", "test@course.com");
      Long missingCourseId = 999L;
      String enrollmentJson = validEnrollmentJson(user.getUserId(), missingCourseId);

      status = mockMvc.perform(post("/api/enrollments")
              .contentType(MediaType.APPLICATION_JSON)
              .content(enrollmentJson))
          .andReturn()
          .getResponse()
          .getStatus();

      enrollmentCountAfterRequest = enrollmentRepository.count();
    }

    @Test
    void shouldReturnNotFoundStatus() {
      assertEquals(404, status);
    }

    @Test
    void shouldNotSaveEnrollment() {
      assertEquals(0, enrollmentCountAfterRequest);
    }
  }

  @Nested
  class GetEnrollmentsForMissingUser {

    private int status;

    @BeforeEach
    void setup() throws Exception {
      Long missingUserId = 999L;
      status = mockMvc.perform(get("/api/enrollments/" + missingUserId))
          .andReturn()
          .getResponse()
          .getStatus();
    }

    @Test
    void shouldReturnNotFoundStatus() {
      assertEquals(404, status);
    }
  }

  private User saveValidUser(String username, String email) {
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setHashedPassword("securePassword123");
    user.setCourseParticipant(false);
    return userRepository.save(user);
  }

  private Course saveValidCourse(User owner) {
    Course course = new Course();
    course.setTitle("Fullstack Web Development");
    course.setShortDescription("Learn Spring Boot + Angular/React");
    course.setDescription("A long and detailed description about coding.");
    course.setPrice(499.00);
    course.setPublished(false);
    course.setLastEdited(LocalDateTime.now());
    course.setOwner(owner);
    return courseRepository.save(course);
  }

  private String validEnrollmentJson(Long userId, Long courseId) {
    return """
                {
                    "userId": %d,
                    "courseId": %d
                }
                """.formatted(userId, courseId);
  }
}