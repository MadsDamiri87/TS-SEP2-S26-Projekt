package com.example.backend.business.dto.mapper;

import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper
{
    public CourseResponse toResponse(Course course)
    {
        return new CourseResponse(
                course.getId(),
                course.getOwner().getUserId(),
                course.getTitle(),
                course.getShortDescription(),
                course.getDescription(),
                course.getPrice(),
                course.isPublished(),
                course.getLastEdited()
        );
    }
}
