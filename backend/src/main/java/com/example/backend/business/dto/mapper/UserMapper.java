package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.user.UpdateUserRequest;
import com.example.backend.business.dto.user.UserProfileResponse;
import com.example.backend.business.dto.user.UserResponse;
import com.example.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class UserMapper
{
  public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getUserId(),
        user.getUsername(),
        user.getEmail(),
        user.isAdministrator(),
        user.isCourseProvider(),
        user.isCourseParticipant()
    );
  }

  public UserProfileResponse toProfileResponse(User user) {
    return new UserProfileResponse(
        user.getUserId(),
        user.getUsername(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getName()
    );
  }

  public void updateUserFromRequest(User user, UpdateUserRequest request) {
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPhoneNumber(request.phoneNumber());
    user.setName(request.name());
  }
}
