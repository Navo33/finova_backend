package com.finance.service;

import com.finance.dto.CategoryDTO;
import com.finance.model.Category;
import com.finance.model.User;
import com.finance.repository.CategoryRepository;
import com.finance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        validateCategoryType(categoryDTO.getCategoryType());

        User user = userRepository.findById(categoryDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (categoryRepository.existsByUserAndCategoryTypeAndCategoryName(
                user,
                categoryDTO.getCategoryType(),
                categoryDTO.getCategoryName())) {
            throw new CategoryAlreadyExistsException("Category already exists");
        }

        Category category = new Category();
        category.setUser(user);
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryType(categoryDTO.getCategoryType());

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);

    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {

        User user = userRepository.findById(categoryDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Category existingcategory = categoryRepository.findByIdAndUser(categoryDTO.getCategoryId(), user)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (categoryRepository.findByUserAndCategoryName(user, categoryDTO.getCategoryName()).isPresent()) {
            throw new CategoryAlreadyExistsException("A category with this name already exists");
        }

        existingcategory.setCategoryName(categoryDTO.getCategoryName());
        existingcategory.setCategoryType(categoryDTO.getCategoryType());

        Category updatedCategory = categoryRepository.save(existingcategory);
        return convertToDto(updatedCategory);

    }

    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }

    public class CategoryAlreadyExistsException extends RuntimeException {
        public CategoryAlreadyExistsException(String message) {
            super(message);
        }
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public class CategoryNotFoundException extends RuntimeException {
        public CategoryNotFoundException(String message) {
            super(message);
        }
    }

    public List<CategoryDTO> getCategoriesByUserId(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Category> categories = categoryRepository.findByUser(user);

        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategoriesByType(UUID userId, Category.CategoryType categoryType) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Category> categories = categoryRepository.findByUserAndCategoryType(user, categoryType);

        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private void validateCategoryType(Category.CategoryType categoryType) {
        if (categoryType == null){
            throw new IllegalArgumentException("Category type cannot be null");
        }
    }

    private CategoryDTO convertToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName(category.getCategoryName());
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryType(category.getCategoryType());
        categoryDTO.setUserId(category.getUser().getUserId());

        return categoryDTO;
    }
}
