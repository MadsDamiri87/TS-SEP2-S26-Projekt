package com.example.backend.business.service;

import com.example.backend.business.dto.course.CourseRequest;
import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.business.dto.mapper.CourseMapper;
import com.example.backend.entity.Course;
import com.example.backend.entity.User;
import com.example.backend.persistence.repository.CourseRepository;
import com.example.backend.persistence.repository.UserRepository;
import com.example.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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

        // set user to be course provider.. and save
        owner.setCourseProvider(true);
        userRepository.save(owner);

        return courseMapper.toResponse(savedCourse);
    }

    public CourseResponse publishCourse(long courseId)
    {
        Course course = getCourse(courseId);

        course.publishCourse();
        course.setLastEditedToNow();

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toResponse(savedCourse);
    }

    public List<CourseResponse> getAllPublishedCourses(){
        List<Course> courses = courseRepository.findCoursesByIsPublishedIs(true);
        return mapToCourseResponses(courses);
    }

    public CourseResponse getCourseById(long courseId)
    {
        Course course = getCourse(courseId);
        return courseMapper.toResponse(course);
    }

    private List<CourseResponse> mapToCourseResponses(List<Course> courses)
    {
        List<CourseResponse> responses = new ArrayList<>();

        for(Course course : courses)
        {
            responses.add(courseMapper.toResponse(course));
        }
        return responses;
    }

    private Course getCourse(long courseId)
    {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("No course found with id = " + courseId));
    }
}
