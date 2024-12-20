package com.finance.model;

import jakarta.persistence.*;

import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private UUID categoryId;

    @Column(unique=true, nullable = false)
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType categoryType;

    public enum CategoryType {
        INCOME,
        EXPENSE
    }

    @ManyToOne
    @JoinColumn(name = "userId",nullable = true)
    private User user;

    // Default constructor
    public Category() {}

    // Getters and Setters
    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryType='" + categoryType + '\'' +
                '}';
    }

}
