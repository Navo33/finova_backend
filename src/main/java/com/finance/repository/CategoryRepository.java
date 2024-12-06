package com.finance.repository;

import com.finance.model.Category;
import com.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByType(String type);
    List<Category> findByUser(User user);
    List<Category> findByUserAndType(User user, String type);

}