package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.user.UserResponse;
import com.example.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper
{
  public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.isAdministrator(),
        user.isCourseProvider(),
        user.isCourseParticipant()
    );
  }

}
