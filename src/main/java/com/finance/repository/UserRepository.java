package com.finance.repository;

import com.finance.model.Role;
import com.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByUser(User user);
    List<User> findByUser(User user, Pageable pageable);

    // user query?
}