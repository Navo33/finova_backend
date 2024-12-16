package com.finance.controller;

import com.finance.dto.CategoryDTO;
import com.finance.model.Category;
import com.finance.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category Management", description = "API for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Create a new category", description = "Add a new category to the system")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO addedCategory = categoryService.addCategory(categoryDTO);
        return ResponseEntity.ok(addedCategory);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update an existing Category", description = "Updates and existing category in the system")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryDTO categoryDTO) {

        categoryDTO.setCategoryId(categoryId);
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete a Category", description = "Delete a category by its ID")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get categories by user", description = "Retrieves categories for a specific user")
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @PathVariable UUID userId) {
        List<CategoryDTO> categories = categoryService.getCategoriesByUserId(userId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/user/{userId}/categoryType/{categoryType}")
    @Operation(summary = "Get categories by categoryType", description = "Retrieves categories for a user in a specific category type")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByCategoryType(
            @PathVariable UUID userId,
            @PathVariable Category.CategoryType categoryType) {
        List<CategoryDTO> categoriesByType = categoryService.getCategoriesByType(userId, categoryType);
        return ResponseEntity.ok(categoriesByType);
    }


}
