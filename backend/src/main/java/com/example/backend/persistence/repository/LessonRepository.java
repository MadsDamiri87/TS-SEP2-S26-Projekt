package com.example.backend.persistence.repository;

import com.example.backend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>
{
    List<Lesson> findAllByModule_ModuleId(Long moduleModuleId);
}
