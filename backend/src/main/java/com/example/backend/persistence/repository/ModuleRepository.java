package com.example.backend.persistence.repository;

import com.example.backend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long>
{
    List<Module> findAllByCourse_Id(Long courseId);

    @Query("SELECT COALESCE(MAX(m.orderNumber), 0) FROM Module m WHERE m.course.id = :courseId")
    int findHighestOrderNumberByCourseId(@Param("courseId") Long courseId);
}
