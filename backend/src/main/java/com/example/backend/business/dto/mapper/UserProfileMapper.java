package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.user.UserProfileResponse;
import com.example.backend.entity.User;

public class UserProfileMapper
{
  public UserProfileResponse toProfileResponse(User user) {
    return new UserProfileResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getName()
    );
  }

}
