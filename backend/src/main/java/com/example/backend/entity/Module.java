package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "modules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_module_course_order",
                        columnNames = {"course_id", "order_number"}
                )
        }
)
public class Module
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(
            mappedBy = "module",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Lesson> lessons = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "order_number", nullable = false)
    private int orderNumber;

    public Module()
    {
    }

    public void addLesson(Lesson lesson)
    {
        lessons.add(lesson);
        lesson.setModule(this);
    }

    public void removeLesson(Lesson lesson)
    {
        lessons.remove(lesson);
        lesson.setModule(null);
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse(Course course)
    {
        this.course = course;
    }

    public List<Lesson> getLessons()
    {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons)
    {
        this.lessons.clear();

        for (Lesson lesson : lessons)
        {
            addLesson(lesson);
        }
    }

    public int getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Long getModuleId()
    {
        return moduleId;
    }

    public void setModuleId(Long moduleId)
    {
        this.moduleId = moduleId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setId(Long id)
    {
        this.moduleId = id;
    }

    public Long getId()
    {
        return moduleId;
    }

}
