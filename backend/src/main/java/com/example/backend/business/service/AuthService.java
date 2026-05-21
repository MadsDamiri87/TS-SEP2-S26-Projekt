package com.example.backend.business.service;

import com.example.backend.business.dto.auth.LoginRequest;
import com.example.backend.business.dto.auth.RegisterUserRequest;
import com.example.backend.business.dto.mapper.UserMapper;
import com.example.backend.business.dto.user.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.UserRepository;
import com.example.backend.shared.exception.ConflictException;
import com.example.backend.shared.exception.InvalidCredentialsException;
import com.example.backend.shared.util.PasswordHasher;
import org.springframework.stereotype.Service;

@Service public class AuthService
{
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordHasher passwordHasher;

  public AuthService(UserRepository userRepository, UserMapper userMapper,
                     PasswordHasher passwordHasher)
  {
    this.userRepository = userRepository;
    this.userMapper     = userMapper;
    this.passwordHasher = passwordHasher;
  }

  public UserResponse registerUser(RegisterUserRequest request)
  {
    boolean usernameExist = userRepository.existsByUsername(request.username());
    boolean emailExist = userRepository.existsByEmail(request.email());

    if (usernameExist && emailExist)
      throw new ConflictException("Username and email already exists");

    if (userRepository.existsByUsername(request.username()))
      throw new ConflictException("Username already exists");

    if (userRepository.existsByEmail(request.email()))
      throw new ConflictException("Email already exists");

    User newUser = new User();
    newUser.setUsername(request.username());
    newUser.setEmail(request.email());

    String hashedPassword = passwordHasher.hash(request.password());
    newUser.setHashedPassword(hashedPassword);

    User registeredUser = userRepository.save(newUser);

    return userMapper.toResponse(registeredUser);
  }

  public UserResponse login(LoginRequest request)
  {
    User user = userRepository.findByUsername(request.username())
                              .orElseThrow(InvalidCredentialsException::new);

    if (!passwordHasher.matches(request.password(), user.getHashedPassword()))
    {
      throw new InvalidCredentialsException();
    }

    return userMapper.toResponse(user);
  }
}
