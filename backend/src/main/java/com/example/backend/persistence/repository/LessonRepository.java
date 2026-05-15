package com.example.backend.persistence.repository;

import com.example.backend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>
{
    List<Lesson> findAllByModule_ModuleId(Long moduleModuleId);

    @Query("SELECT COALESCE(MAX(l.orderNumber), 0) FROM Lesson l WHERE l.module.moduleId = :moduleId")
    int findHighestOrderNumberByModuleId(@Param("moduleId") Long moduleId);

    List<Lesson> findAllByModule_ModuleIdAndOrderNumberGreaterThan(
            Long moduleId,
            int orderNumber
    );
}
