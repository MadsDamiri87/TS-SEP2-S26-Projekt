package com.example.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity @Table(name = "enrollments", uniqueConstraints = {
@UniqueConstraint(columnNames = {"user_id", "course_id"})})

public class Enrollment
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long enrollmentId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  private LocalDateTime enrolledAt;

  public Enrollment()
  {
  }

  public void setEnrollmentId(Long enrollmentId)
  {
    this.enrollmentId = enrollmentId;
  }

  public void setUser(User user)
  {
    this.user = user;
  }

  public void setCourse(Course course)
  {
    this.course = course;
  }

  public Long getEnrollmentId()
  {
    return enrollmentId;
  }

  public User getUser()
  {
    return user;
  }

  public Course getCourse()
  {
    return course;
  }

  public LocalDateTime getEnrolledAt()
  {
    return enrolledAt;
  }

  public void setEnrolledAt(LocalDateTime enrolledAt)
  {
    this.enrolledAt = enrolledAt;
  }

  public void setEnrolledAt()
  {
    this.enrolledAt = LocalDateTime.now();
  }
}
