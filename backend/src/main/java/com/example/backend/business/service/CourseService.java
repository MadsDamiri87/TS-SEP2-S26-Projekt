package com.example.backend.business.service;

import com.example.backend.business.dto.course.CourseRequest;
import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.business.dto.mapper.CourseMapper;
import com.example.backend.entity.Course;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.UserRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;


public class CourseService
{
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(UserRepository userRepository, CourseRepository courseRepository, CourseMapper courseMapper)
    {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course();

        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id = " + request.ownerId()));

        course.setOwner(owner);
        course.setTitle(request.title());
        course.setShortDescription(request.shortDescription());
        course.setDescription(request.description());
        course.setPrice(request.price());
        course.setPublished(false);
        course.setLastEditedToNow();

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }
}
