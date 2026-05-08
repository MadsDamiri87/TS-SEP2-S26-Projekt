package com.example.backend.business.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"userId", "username", "email", "isAdministrator", "isCourseProvider", "isCourseParticipant"})
public record UserResponse(
        long userId,
        String username,
        String email,
        boolean isAdministrator,
        boolean isCourseProvider,
        boolean isCourseParticipant
)
{
}
