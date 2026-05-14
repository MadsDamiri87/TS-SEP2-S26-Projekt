package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<Course> ownedCourses = new ArrayList<>();

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String hashedPassword;

    @Column(nullable = false, length = 100, unique = true)
    private String email;
    
    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 200)
    private String name;

    @Column
    private boolean isAdministrator;

    @Column
    private boolean isCourseProvider;

    @Column
    private boolean isCourseParticipant;

    public User()
    {
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public List<Course> getOwnedCourses()
    {
        return ownedCourses;
    }

    public void setOwnedCourses(List<Course> courses)
    {
        this.ownedCourses = courses;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAdministrator() {
    return isAdministrator;
  }

  public void setAdministrator(boolean administrator) {
    isAdministrator = administrator;
  }

  public boolean isCourseProvider() {
    return isCourseProvider;
  }

  public void setCourseProvider(boolean courseProvider) {
    isCourseProvider = courseProvider;
  }

  public boolean isCourseParticipant() {
    return isCourseParticipant;
  }

  public void setCourseParticipant(boolean courseParticipant) {
    isCourseParticipant = courseParticipant;
  }
}
