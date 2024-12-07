package com.finance.repository;

import com.finance.model.Role;
import com.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByRole(Role role);
    long countByRole(Role role);

}