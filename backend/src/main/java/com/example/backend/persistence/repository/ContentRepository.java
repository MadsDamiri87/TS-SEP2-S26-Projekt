package com.example.backend.persistence.repository;

import com.example.backend.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, Long>
{
    @Query("""
                SELECT COALESCE(MAX(c.orderNumber), 0)
                FROM Content c
                WHERE c.lesson.lessonId = :lessonId
            """)
    int findHighestOrderNumberByLessonId(@Param("lessonId") Long lessonId);
}
