package com.example.backend.integration;

import com.example.backend.entity.User;
import com.example.backend.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") class UserProfileIntegrationTest
{

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  private Long savedUserId;

  // Test bruger felter
  private static final long UNKNOWN_USER_ID = 99L;
  private static final String USERNAME = "jdoe";
  private static final String EMAIL = "jane@example.com";
  private static final String PHONE_NUMBER = "12345678";
  private static final String NAME = "Jane Doe";
  private static final String PASSWORD = "hashed_password";

  private static final String UPDATED_USERNAME = "jdoe_updated";
  private static final String UPDATED_EMAIL = "updated@example.com";
  private static final String UPDATED_PHONE = "87654321";
  private static final String UPDATED_NAME = "Jane Updated";

  private User buildUser()
  {
    User user = new User();
    user.setUsername(USERNAME);
    user.setEmail(EMAIL);
    user.setPhoneNumber(PHONE_NUMBER);
    user.setName(NAME);
    user.setHashedPassword(PASSWORD);
    return user;
  }

  @BeforeEach void setUp()
  {
    userRepository.deleteAll();
    savedUserId = userRepository.save(buildUser()).getId();
  }

  @Test void getUserProfile_returnsUserProfile_whenUserExists() throws Exception
  {
    mockMvc.perform(get("/api/user/" + savedUserId)).andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(USERNAME))
        .andExpect(jsonPath("$.email").value(EMAIL))
        .andExpect(jsonPath("$.phoneNumber").value(PHONE_NUMBER))
        .andExpect(jsonPath("$.name").value(NAME));
  }
}