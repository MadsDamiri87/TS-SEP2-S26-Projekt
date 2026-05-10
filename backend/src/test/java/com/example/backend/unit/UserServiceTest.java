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

  // Hjælpemetoder

  private User buildUser(long userId) {
    User user = new User();
    user.setId(userId);
    user.setUsername("jdoe");
    user.setEmail("jane@example.com");
    user.setPhoneNumber("12345678");
    user.setName("Jane Doe");
    return user;
  }

  private UserProfileResponse buildProfileResponse(long userId) {
    return new UserProfileResponse(userId, "jdoe", "jane@example.com", "12345678", "Jane Doe");
  }

  // getUserProfile(userId)

  @Test
  void getUserProfile_returnsProfile_whenUserExists() {
    // Arrange
    User user = buildUser(1L);
    UserProfileResponse expected = buildProfileResponse(1L);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toProfileResponse(user)).thenReturn(expected);

    // Act
    UserProfileResponse result = userService.getUserProfile(1L);

    // Assert
    assertThat(result.userId()).isEqualTo(1L);
    assertThat(result.username()).isEqualTo("jdoe");
    assertThat(result.email()).isEqualTo("jane@example.com");
  }

  @Test
  void getUserProfile_throwsResourceNotFoundException_whenUserNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUserProfile(99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
  }

  // updateUserProfile(request, userId)

  @Test
  void updateUserProfile_savesAndReturnsUpdatedProfile_whenUserExists() {
    // Arrange
    User user = buildUser(1L);
    UpdateUserRequest request = new UpdateUserRequest(
        "jdoe_updated", "updated@example.com", "87654321", "Jane Updated"
    );
    UserProfileResponse expected = new UserProfileResponse(
        1L, "jdoe_updated", "updated@example.com", "87654321", "Jane Updated"
    );

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toProfileResponse(user)).thenReturn(expected);

    // Act
    UserProfileResponse result = userService.updateUserProfile(request, 1L);

    // Assert
    verify(userMapper, times(1)).updateUserFromRequest(user, request);
    verify(userRepository, times(1)).save(user);
    assertThat(result.username()).isEqualTo("jdoe_updated");
    assertThat(result.email()).isEqualTo("updated@example.com");
  }

  @Test
  void updateUserProfile_throwsResourceNotFoundException_whenUserNotFound() {
    UpdateUserRequest request = new UpdateUserRequest(
        "ghost", "ghost@example.com", null, null
    );

    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.updateUserProfile(request, 99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
  }
}