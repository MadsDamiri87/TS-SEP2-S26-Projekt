package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "lessons",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "lesson_module_order",
                        columnNames = {"module_id", "order_number"}
                )
        }
)
public class Lesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @OneToMany(
            mappedBy = "lesson",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Content> contents = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "order_number", nullable = false)
    private int orderNumber;


    public Lesson()
    {
    }

    public void addContent(Content content) {
        contents.add(content);
        content.setLesson(this);
    }

    public void removeContent(Content content) {
        contents.remove(content);
        content.setLesson(null);
    }

    public int getOrderNumber()
    {
        return orderNumber;
    }

    public List<Content> getContents()
    {
        return contents;
    }

    public void setContents(List<Content> contents)
    {
        this.contents = contents;
    }

    public void setOrderNumber(int orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Long getLessonId()
    {
        return lessonId;
    }

    public void setLessonId(Long lessonId)
    {
        this.lessonId = lessonId;
    }

    public Module getModule()
    {
        return module;
    }

    public void setModule(Module module)
    {
        this.module = module;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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
        this.lessonId = id;
    }

    public Long getId()
    {
        return lessonId;
    }
}
