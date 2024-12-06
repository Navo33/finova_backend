package com.finance.repository;


import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUser(User user);
    List<Transaction> findByUser(User user, Pageable pageable);

    List<Transaction> findByUserAndCategory(User user, Category category);
    List<Transaction> findByUserAndCategory(User user, Category category, Pageable pageable);

    List<Transaction> findByCategory(Category category);
    List<Transaction> findByCategory(Category category, Pageable pageable);

    Double sumAmountByUserAndTransactionType(User user, Transaction.TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}