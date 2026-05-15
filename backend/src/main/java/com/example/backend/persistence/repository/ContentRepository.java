package com.example.backend.persistence.repository;

import com.example.backend.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long>
{
    int findHighestOrderNumberByLesson_LessonId(Long lessonId);
}
