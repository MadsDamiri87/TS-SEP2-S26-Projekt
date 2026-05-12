package com.example.backend.business.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({"username", "email", "phoneNumber", "name"})
public record UpdateUserRequest(
    @NotBlank @Size(max = 30)          String username,
    @NotBlank @Email @Size(max = 100)  String email,
    @Size(max = 20)                    String phoneNumber,
    @Size(max = 200)                   String name
)
{}