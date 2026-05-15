package com.example.backend.persistence.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long >
{
  List<Enrollment> findByUser_UserId(Long userId);

  List<Enrollment> findByCourse_Id(Long courseId);
}

