package com.finance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction",
       indexes = {
           @Index(name = "idx_tx_user_date", columnList = "userId, date"),
           @Index(name = "idx_tx_category", columnList = "categoryId"),
           @Index(name = "idx_tx_user_category", columnList = "userId, categoryId")
       })
public class Transaction {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(nullable = false, precision = 15, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private boolean active = true;               // Soft-delete flag

    // -----------------------------------------------------------------
    // Helper methods for bidirectional sync
    // -----------------------------------------------------------------
    public void setUser(User user) {
        this.user = user;
        if (user != null) user.addTransaction(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category != null) category.addTransaction(this);
    }

    // -----------------------------------------------------------------
    // Constructors, getters & setters
    // -----------------------------------------------------------------
    public Transaction() {}

    public UUID getTransactionId() { return transactionId; }
    public void setTransactionId(UUID transactionId) { this.transactionId = transactionId; }

    public User getUser() { return user; }
    public Category getCategory() { return category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Transaction{id=" + transactionId +
               ", amount=" + amount +
               ", desc='" + description + '\'' +
               ", date=" + date +
               ", active=" + active +
               '}';
    }
}