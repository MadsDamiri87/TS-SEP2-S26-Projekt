package com.example.backend.persistence.repository;

import com.example.backend.business.dto.course.CourseResponse;
import com.example.backend.entity.Course;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>
{
    List<Course> findCoursesByIsPublishedIs(boolean isPublished);
    List<Course> findByOwner(User owner);
}
