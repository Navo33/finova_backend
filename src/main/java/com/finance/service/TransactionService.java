package com.finance.service;

import com.finance.dto.TransactionDTO;
import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.User;
import com.finance.repository.CategoryRepository;
import com.finance.repository.TransactionRepository;
import com.finance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {

        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDate(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToDto(savedTransaction);
    }

    @Transactional
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {

        Transaction existingTransaction = transactionRepository.findById(transactionDTO.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingTransaction.setCategory(category);
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setDescription(transactionDTO.getDescription());

        if (existingTransaction.getAmount() <= 0) {
            throw new InvalidTransactionException("Amount must be positive");
        }


        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return convertToDto(updatedTransaction);

    }

    @Transactional
    public void deleteTransaction(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        transactionRepository.delete(transaction);
    }

    public class TransactionNotFoundException extends RuntimeException {
        public TransactionNotFoundException(String message) {
            super(message);
        }
    }

    public class InvalidTransactionException extends RuntimeException {
        public InvalidTransactionException(String message) {
            super(message);
        }
    }

    public List<TransactionDTO> getTransactionsByUser(UUID userId, int page, int size, String sortBy) {

         Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUser(user, pageable);

        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    public List<TransactionDTO> getTransactionsByDateRange(UUID userId, LocalDateTime start, LocalDateTime end) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUserAndDateBetween(user, start, end);

        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public List<TransactionDTO> getTransactionsByCategory(UUID userId, UUID categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Transaction> transactions = transactionRepository.findByUserAndCategory(user, category);

        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public double calculateTotalByType(UUID userId, Transaction.TransactionType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.sumAmountByUserAndTransactionType(user, type);
    }

    private TransactionDTO convertToDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setUserId(transaction.getUser().getUserId());
        transactionDTO.setCategoryId(transaction.getCategory().getCategoryId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setDate(transaction.getDate());
        return transactionDTO;
    }
}