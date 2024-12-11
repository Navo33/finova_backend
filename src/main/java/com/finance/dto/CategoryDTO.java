package com.finance.dto;

import com.finance.model.Category;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CategoryDTO {

    private UUID categoryId;

    @NotNull(message = "Category Name is Required")
    private String categoryName;

    @NotNull(message = "Category Type is required")
    private Category.CategoryType categoryType;

    @NotNull(message = "User Id is required")
    private UUID userId;

    public CategoryDTO() {}

    public CategoryDTO(UUID categoryId, String categoryName, Category.CategoryType categoryType, UUID userId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.userId = userId;
    }

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

    public Category.CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Category.CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
