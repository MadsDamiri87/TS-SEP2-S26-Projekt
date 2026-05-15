package com.example.backend.business.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({"username", "email", "phoneNumber", "name"})
public record UpdateUserRequest(
    @NotBlank(message = "Username must not be blank")
    @Size(max = 30, message = "Username must be at most 30 characters") String username,
    @NotBlank(message = "Email must not be blank") @Email(message = "Email not formatted correctly") @Size(max = 100) String email,
    @Size(max = 20) String phoneNumber, @Size(max = 200) String name)
{
}