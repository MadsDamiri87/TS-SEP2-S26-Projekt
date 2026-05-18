package com.example.backend.integration;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UpdateUserProfileIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  // Inject dependencies causing the FK constraint violations
  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @BeforeEach
  void cleanDatabase() {
    // Delete child rows first to satisfy foreign key constraints
    enrollmentRepository.deleteAll();
    courseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Nested
  class GetUserProfileWithValidId {

    private int status;

    @BeforeEach
    void setup() throws Exception {
      User user = saveValidUser();

      status = mockMvc.perform(get("/api/user/" + user.getUserId()))
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
  class GetUserProfileWithMissingId {

    private int status;

    @BeforeEach
    void setup() throws Exception {
      User user = saveValidUser();
      long nonExistentId = user.getUserId() + 100;

      status = mockMvc.perform(get("/api/user/" + nonExistentId))
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
  class UpdateUserProfileWithValidInput {

    private User updatedUser;
    private int status;

    @BeforeEach
    void setup() throws Exception {
      User user = saveValidUser();

      status = mockMvc.perform(put("/api/user/" + user.getUserId())
              .contentType(MediaType.APPLICATION_JSON)
              .content(validUpdateUserJson()))
          .andReturn()
          .getResponse()
          .getStatus();

      updatedUser = userRepository.findById(user.getUserId()).orElseThrow();
    }

    @Test
    void shouldReturnOkStatus() {
      assertEquals(200, status);
    }

    @Test
    void shouldPersistUpdatedUsername() {
      assertEquals("emil_updated", updatedUser.getUsername());
    }

    @Test
    void shouldPersistUpdatedEmail() {
      assertEquals("emil_updated@test.com", updatedUser.getEmail());
    }

    @Test
    void shouldPersistUpdatedPhoneNumber() {
      assertEquals("87654321", updatedUser.getPhoneNumber());
    }

    @Test
    void shouldPersistUpdatedName() {
      assertEquals("Emil Opdateret", updatedUser.getName());
    }
  }

  @Nested
  class UpdateUserProfileWithMissingUser {

    private int status;

    @BeforeEach
    void setup() throws Exception {
      User user = saveValidUser();
      long nonExistentId = user.getUserId() + 100;

      status = mockMvc.perform(put("/api/user/" + nonExistentId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(validUpdateUserJson()))
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
  class UpdateUserProfileWithInvalidInput {

    private User user;

    @BeforeEach
    void setup() {
      user = saveValidUser();
    }

    @Test
    void shouldReturnBadRequestWhenUsernameIsBlank() throws Exception {
      int status = mockMvc.perform(put("/api/user/" + user.getUserId())
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                                    {
                                        "username": "",
                                        "email": "emil@test.com",
                                        "phoneNumber": "12345678",
                                        "name": "Emil Hansen"
                                    }
                                    """))
          .andReturn()
          .getResponse()
          .getStatus();

      assertEquals(400, status);
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
      int status = mockMvc.perform(put("/api/user/" + user.getUserId())
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                                    {
                                        "username": "emil",
                                        "email": "not-an-email",
                                        "phoneNumber": "12345678",
                                        "name": "Emil Hansen"
                                    }
                                    """))
          .andReturn()
          .getResponse()
          .getStatus();

      assertEquals(400, status);
    }

    @Test
    void shouldReturnBadRequestWhenUsernameExceedsMaxLength() throws Exception {
      int status = mockMvc.perform(put("/api/user/" + user.getUserId())
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                                    {
                                        "username": "%s",
                                        "email": "emil@test.com",
                                        "phoneNumber": "12345678",
                                        "name": "Emil Hansen"
                                    }
                                    """.formatted("a".repeat(31))))
          .andReturn()
          .getResponse()
          .getStatus();

      assertEquals(400, status);
    }
  }

  private User saveValidUser() {
    User user = new User();
    user.setUsername("emil");
    user.setEmail("emil@test.com");
    user.setHashedPassword("hashed-password");
    user.setPhoneNumber("12345678");
    user.setName("Emil Hansen");
    user.setCourseProvider(false);
    return userRepository.save(user);
  }

  private String validUpdateUserJson() {
    return """
                {
                    "username":    "emil_updated",
                    "email":       "emil_updated@test.com",
                    "phoneNumber": "87654321",
                    "name":        "Emil Opdateret"
                }
                """;
  }
}