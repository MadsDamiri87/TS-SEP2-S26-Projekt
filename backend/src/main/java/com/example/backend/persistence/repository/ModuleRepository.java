package com.example.backend.persistence.repository;

import com.example.backend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long>
{
    List<Module> findAllByCourse_Id(Long courseId);
}
