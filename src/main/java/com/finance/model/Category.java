package com.finance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category",
       uniqueConstraints = @UniqueConstraint(columnNames = {"categoryName", "userId"}))
public class Category {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID categoryId;

    @Column(nullable = false, length = 50)
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private CategoryType categoryType;

    public enum CategoryType { INCOME, EXPENSE }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = true)   // null → system category
    private User user;

    // -----------------------------------------------------------------
    // Back reference to transactions (optional but handy for analytics)
    // -----------------------------------------------------------------
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // -----------------------------------------------------------------
    // Helper methods
    // -----------------------------------------------------------------
    public void addTransaction(Transaction t) {
        transactions.add(t);
        t.setCategory(this);
    }

    public void removeTransaction(Transaction t) {
        transactions.remove(t);
        t.setCategory(null);
    }

    // -----------------------------------------------------------------
    // Constructors, getters & setters
    // -----------------------------------------------------------------
    public Category() {}

    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public CategoryType getCategoryType() { return categoryType; }
    public void setCategoryType(CategoryType categoryType) { this.categoryType = categoryType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    @Override
    public String toString() {
        return "Category{categoryId=" + categoryId +
               ", name='" + categoryName + '\'' +
               ", type=" + categoryType +
               ", userId=" + (user != null ? user.getUserId() : "SYSTEM") +
               '}';
    }
}