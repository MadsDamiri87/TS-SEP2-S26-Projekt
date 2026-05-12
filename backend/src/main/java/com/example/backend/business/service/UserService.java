package com.example.backend.business.service;

import com.example.backend.business.dto.mapper.UserMapper;
import com.example.backend.business.dto.user.UpdateUserRequest;
import com.example.backend.business.dto.user.UserProfileResponse;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.UserRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserProfileResponse getUserProfile(long userId)
  {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));

    return userMapper.toProfileResponse(user);
  }

  public UserProfileResponse updateUserProfile(UpdateUserRequest request, long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));

    User conflictingUser = userRepository.findByUsername(request.username()).orElse(null);
    if (conflictingUser != null && !conflictingUser.getId().equals(userId)) {
      throw new IllegalArgumentException("Username already exists");
    }

    userMapper.updateUserFromRequest(user, request);

    userRepository.save(user);

    return userMapper.toProfileResponse(user);
  }
}
