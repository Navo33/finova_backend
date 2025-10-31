package com.finance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user",
       indexes = {
           @Index(name = "idx_user_email", columnList = "email"),
           @Index(name = "idx_user_username", columnList = "username")
       })
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID userId;

    @Column(nullable = false, unique = true, length = 20)
    @Size(min = 5, max = 20, message = "Username must be 5-20 characters")
    private String username;

    @Column(nullable = false, length = 255)
    private String password;                     // **BCrypt hash** (set in service)

    @Column(length = 20)
    @Size(max = 20)
    private String firstName;

    @Column(length = 20)
    @Size(max = 20)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // -----------------------------------------------------------------
    // Bidirectional relationships
    // -----------------------------------------------------------------
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "active = true")
    private List<Transaction> transactions = new ArrayList<>();

    // -----------------------------------------------------------------
    // Lifecycle
    // -----------------------------------------------------------------
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // -----------------------------------------------------------------
    // Helper methods for bidirectional sync
    // -----------------------------------------------------------------
    public void addCategory(Category c) {
        categories.add(c);
        c.setUser(this);
    }

    public void removeCategory(Category c) {
        categories.remove(c);
        c.setUser(null);
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        t.setUser(this);
    }

    public void removeTransaction(Transaction t) {
        transactions.remove(t);
        t.setUser(null);
    }

    // -----------------------------------------------------------------
    // Constructors, getters & setters
    // -----------------------------------------------------------------
    public User() {}

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    @Override
    public String toString() {
        return "User{userId=" + userId +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", role=" + (role != null ? role.getRoleName() : "null") +
               '}';
    }
}