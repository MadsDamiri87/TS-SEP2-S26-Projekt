package com.example.backend.business.dto.content;

public record ContentResponse(
        Long contentId,
        Long lessonId,
        String originalFileName,
        String contentType,
        int orderNumber,
        String url
)
{
}
