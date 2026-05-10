package com.example.backend.unit;

import com.example.backend.business.dto.mapper.UserMapper;
import com.example.backend.business.dto.user.UpdateUserRequest;
import com.example.backend.business.dto.user.UserProfileResponse;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.UserRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import com.example.backend.business.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserService userService;

  // Test bruger felter
  private static final long   USER_ID          = 1L;
  private static final long   UNKNOWN_USER_ID  = 99L;
  private static final String USERNAME         = "jdoe";
  private static final String EMAIL            = "jane@example.com";
  private static final String PHONE_NUMBER     = "12345678";
  private static final String NAME             = "Jane Doe";

  private static final String UPDATED_USERNAME = "jdoe_updated";
  private static final String UPDATED_EMAIL    = "updated@example.com";
  private static final String UPDATED_PHONE    = "87654321";
  private static final String UPDATED_NAME     = "Jane Updated";

  // Hjælpemetoder

  private User buildUser() {
    User user = new User();
    user.setId(USER_ID);
    user.setUsername(USERNAME);
    user.setEmail(EMAIL);
    user.setPhoneNumber(PHONE_NUMBER);
    user.setName(NAME);
    return user;
  }

  private UserProfileResponse buildProfileResponse() {
    return new UserProfileResponse(USER_ID, USERNAME, EMAIL, PHONE_NUMBER, NAME);
  }

  private UserProfileResponse buildUpdatedProfileResponse() {
    return new UserProfileResponse(USER_ID, UPDATED_USERNAME, UPDATED_EMAIL, UPDATED_PHONE, UPDATED_NAME);
  }

  // getUserProfile(userId)

  @Test
  void getUserProfile_returnsProfile_whenUserExists() {
    // Arrange
    User user = buildUser();
    UserProfileResponse expected = buildProfileResponse();

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(userMapper.toProfileResponse(user)).thenReturn(expected);

    // Act
    UserProfileResponse result = userService.getUserProfile(USER_ID);

    // Assert
    assertThat(result.userId()).isEqualTo(USER_ID);
    assertThat(result.username()).isEqualTo(USERNAME);
    assertThat(result.email()).isEqualTo(EMAIL);
  }

  @Test
  void getUserProfile_throwsResourceNotFoundException_whenUserNotFound() {
    when(userRepository.findById(UNKNOWN_USER_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUserProfile(UNKNOWN_USER_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(String.valueOf(UNKNOWN_USER_ID));
  }

  // updateUserProfile(request, userId)

  @Test
  void updateUserProfile_savesAndReturnsUpdatedProfile_whenUserExists() {
    User user = buildUser();
    UpdateUserRequest request = new UpdateUserRequest(
        UPDATED_USERNAME, UPDATED_EMAIL, UPDATED_PHONE, UPDATED_NAME
    );
    UserProfileResponse expected = buildUpdatedProfileResponse();

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(userMapper.toProfileResponse(user)).thenReturn(expected);

    UserProfileResponse result = userService.updateUserProfile(request, USER_ID);

    verify(userMapper, times(1)).updateUserFromRequest(user, request);
    verify(userRepository, times(1)).save(user);
    assertThat(result.username()).isEqualTo(UPDATED_USERNAME);
    assertThat(result.email()).isEqualTo(UPDATED_EMAIL);
  }

  @Test
  void updateUserProfile_throwsResourceNotFoundException_whenUserNotFound() {
    UpdateUserRequest request = new UpdateUserRequest(
        UPDATED_USERNAME, UPDATED_EMAIL, UPDATED_PHONE, UPDATED_NAME
    );

    when(userRepository.findById(UNKNOWN_USER_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.updateUserProfile(request, UNKNOWN_USER_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(String.valueOf(UNKNOWN_USER_ID));
  }
}