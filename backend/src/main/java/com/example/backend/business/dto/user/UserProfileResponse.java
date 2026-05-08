package com.example.backend.business.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"userId", "username", "email", "phoneNumber",
    "name"}) public record UserProfileResponse(long userId, String username,
                                               String email, String phoneNumber,
                                               String name)
{
}
