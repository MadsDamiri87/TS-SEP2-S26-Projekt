package com.example.backend.unit;

import com.example.backend.business.dto.mapper.UserMapper;
import com.example.backend.business.dto.user.UpdateUserRequest;
import com.example.backend.business.dto.user.UserProfileResponse;
import com.example.backend.business.dto.user.UserResponse;
import com.example.backend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest
{

  private UserMapper userMapper;

  // Test bruger felter
  private static final long USER_ID = 1L;
  private static final String USERNAME = "jdoe";
  private static final String EMAIL = "jane@example.com";
  private static final String PHONE_NUMBER = "12345678";
  private static final String NAME = "Jane Doe";

  @BeforeEach void setUp()
  {
    userMapper = new UserMapper();
  }

  // Hjælpere

  private User buildUser()
  {
    User user = new User();
    user.setId(USER_ID);
    user.setUsername(USERNAME);
    user.setEmail(EMAIL);
    user.setPhoneNumber(PHONE_NUMBER);
    user.setName(NAME);
    return user;
  }

  // toResponse(User user)

  @Test void toResponse_mapsAllFieldsCorrectly()
  {
    User user = buildUser();
    user.setAdministrator(true);
    user.setCourseProvider(false);
    user.setCourseParticipant(true);

    UserResponse response = userMapper.toResponse(user);

    assertThat(response.userId()).isEqualTo(USER_ID);
    assertThat(response.username()).isEqualTo(USERNAME);
    assertThat(response.email()).isEqualTo(EMAIL);
    assertThat(response.isAdministrator()).isTrue();
    assertThat(response.isCourseProvider()).isFalse();
    assertThat(response.isCourseParticipant()).isTrue();
  }

  @Test void toResponse_mapsAllBooleansAsFalse_whenUserHasNoRoles()
  {
    User user = buildUser();
    // booleans defaulter til false i Java, ingen setters nødvendige

    UserResponse response = userMapper.toResponse(user);

    assertThat(response.isAdministrator()).isFalse();
    assertThat(response.isCourseProvider()).isFalse();
    assertThat(response.isCourseParticipant()).isFalse();
  }

  // toProfileResponse(User user)

  @Test void toProfileResponse_mapsAllFieldsCorrectly()
  {
    User user = buildUser();

    UserProfileResponse response = userMapper.toProfileResponse(user);

    assertThat(response.userId()).isEqualTo(USER_ID);
    assertThat(response.username()).isEqualTo(USERNAME);
    assertThat(response.email()).isEqualTo(EMAIL);
    assertThat(response.phoneNumber()).isEqualTo(PHONE_NUMBER);
    assertThat(response.name()).isEqualTo(NAME);
  }

  @Test void toProfileResponse_mapsNullableFieldsAsNull_whenNotSet()
  {
    User user = buildUser();
    user.setPhoneNumber(null);
    user.setName(null);

    UserProfileResponse response = userMapper.toProfileResponse(user);

    assertThat(response.phoneNumber()).isNull();
    assertThat(response.name()).isNull();
  }

  // updateUserFromRequest

  @Test void updateUserFromRequest_updatesAllFieldsOnUser()
  {
    User user = buildUser();
    UpdateUserRequest request = new UpdateUserRequest(
        USERNAME, EMAIL, PHONE_NUMBER, NAME
    );

    userMapper.updateUserFromRequest(user, request);

    assertThat(user.getUsername()).isEqualTo(USERNAME);
    assertThat(user.getEmail()).isEqualTo(EMAIL);
    assertThat(user.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
    assertThat(user.getName()).isEqualTo(NAME);
  }

  @Test void updateUserFromRequest_setsNullableFieldsToNull_whenRequestHasNull()
  {
    User user = buildUser();
    UpdateUserRequest request = new UpdateUserRequest(
        USERNAME, EMAIL, null, null
    );

    userMapper.updateUserFromRequest(user, request);

    assertThat(user.getPhoneNumber()).isNull();
    assertThat(user.getName()).isNull();
  }
}