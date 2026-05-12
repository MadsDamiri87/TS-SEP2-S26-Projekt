package com.example.backend.controller;

import com.example.backend.business.dto.user.UpdateUserRequest;
import com.example.backend.business.dto.user.UserProfileResponse;
import com.example.backend.business.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController
{
  private final UserService userService;

  public UserController(UserService userService){
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserProfileResponse> getUserById(@PathVariable long userId) {
    return ResponseEntity.ok(userService.getUserProfile(userId));
  }

  @PutMapping("/{userId}")
  public ResponseEntity<UserProfileResponse> updateUserById(
      @PathVariable long userId,
      @RequestBody @Valid UpdateUserRequest request) {
    return ResponseEntity.ok(userService.updateUserProfile(request, userId));
  }
}
