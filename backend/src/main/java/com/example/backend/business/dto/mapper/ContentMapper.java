package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.content.ContentResponse;
import com.example.backend.entity.Content;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContentMapper
{
    public ContentResponse toResponse(Content content) {
        return new ContentResponse(
                content.getContentId(),
                content.getLesson().getLessonId(),
                content.getOriginalFileName(),
                content.getContentType().toString(),
                content.getOrderNumber(),
                "/contents/" + content.getContentId() + "/file"
        );
    }

    public List<ContentResponse> toResponse(List<Content> contents) {
        return contents.stream()
                .map(this::toResponse)
                .toList();
    }
}
