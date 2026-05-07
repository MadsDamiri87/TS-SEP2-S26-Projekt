package com.example.backend.business.dto.course;

public record CourseRequest(Long ownerId,
                            String title,
                            String shortDescription,
                            String description,
                            double price)

{
}
