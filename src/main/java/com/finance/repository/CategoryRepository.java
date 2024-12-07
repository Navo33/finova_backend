package com.finance.repository;

import com.finance.model.Category;
import com.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByCategoryType(Category.CategoryType categoryType);
    List<Category> findByUser(User user);
    List<Category> findByUserAndCategoryType(User user, Category.CategoryType categoryType);

    Optional<Category> findByUserAndCategoryTypeAndCategoryName(User user, Category.CategoryType categoryType, String categoryName);
    boolean existsByUserAndCategoryTypeAndCategoryName(User user, Category.CategoryType categoryType, String categoryName);

    List<Category> findByUserOrderByCategoryNameAsc(User user);
    List<Category> findByUserAndCategoryTypeOrderByCategoryNameAsc(User user, Category.CategoryType categoryType);

}