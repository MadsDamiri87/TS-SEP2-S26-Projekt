package com.example.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 130)
    private String shortDescription;

    @Column(nullable = false, length = 800)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean isPublished;

    @Column(nullable = false)
    private LocalDateTime lastEdited;

    public Course()
    {
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public boolean isPublished()
    {
        return isPublished;
    }

    public void setPublished(boolean published)
    {
        isPublished = published;
    }

    public LocalDateTime getLastEdited()
    {
        return lastEdited;
    }

    public void setLastEdited(LocalDateTime lastEdited)
    {
        this.lastEdited = lastEdited;
    }

    public void setLastEditedToNow() {
        lastEdited = LocalDateTime.now();
    }
}
