package com.finance.controller;

import com.finance.dto.TransactionDTO;
import com.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController

@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO addedTransaction = transactionService.addTransaction(transactionDTO);
        return ResponseEntity.created(
                URI.create("api/transactions/"+addedTransaction.getTransactionId())
        ).body(addedTransaction);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByUser(userId, page, size, sortBy);
        return ResponseEntity.ok(transactions);
    }

}