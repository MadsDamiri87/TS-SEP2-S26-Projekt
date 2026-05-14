package com.example.backend.persistence.repository;

import com.example.backend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long>
{
}
