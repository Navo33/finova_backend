package com.finance.repository;


import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUser(User user);
    List<Transaction> findByUser(User user, Pageable pageable);

    List<Transaction> findByUserAndCategory(User user, Category category);
    List<Transaction> findByUserAndCategory(User user, Category category, Pageable pageable);

    List<Transaction> findByCategory(Category category);
    List<Transaction> findByCategory(Category category, Pageable pageable);

}