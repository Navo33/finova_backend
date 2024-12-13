package com.finance.controller;

import com.finance.dto.TransactionDTO;
import com.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.InvalidTransactionException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Management", description = "APIs for managing transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Adds a new transaction to the system")
    public ResponseEntity<TransactionDTO> addTransaction(
            @Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO addedTransaction = transactionService.addTransaction(transactionDTO);
        return ResponseEntity.created(
                URI.create("api/transactions/" + addedTransaction.getTransactionId())
        ).body(addedTransaction);
    }

    @PutMapping("/{transactionId}")
    @Operation(summary = "Update an existing transaction", description = "Updates an existing transaction in the system")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable UUID transactionId,
            @Valid @RequestBody TransactionDTO transactionDTO) {
        try {
            transactionDTO.setTransactionId(transactionId);
            TransactionDTO updatedTransaction = transactionService.updateTransaction(transactionDTO);
            return ResponseEntity.ok(updatedTransaction);
        } catch (InvalidTransactionException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Delete a transaction", description = "Deletes a transaction by its ID")
    public ResponseEntity<TransactionDTO> deleteTransaction(@PathVariable UUID transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get transactions by user", description = "Retrieves paginated transactions for a specific user")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByUser(userId, page, size, sortBy);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("user/{userId}/date-range")
    @Operation(summary = "Get transactions by date range", description = "Retrieves transactions for a user within a specific date range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(userId, start, end);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    @Operation(summary = "Get transactions by category", description = "Retrieves transactions for a user in a specific category")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCategory(
            @PathVariable UUID userId,
            @PathVariable UUID categoryId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCategory(userId, categoryId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}/total")
    @Operation(summary = "Calculate total transactions by type", description = "Calculates total transaction amount for a user by transaction type")
    public ResponseEntity<Double> calculateTotalByType(
            @PathVariable UUID userId,
            @RequestParam com.finance.model.Transaction.TransactionType type) {
        double total = transactionService.calculateTotalByType(userId, type);
        return ResponseEntity.ok(total);
    }
}
