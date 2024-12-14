package com.finance.controller;

import com.finance.dto.UserDTO;
import com.finance.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController

@RequestMapping("/api/users")
public class UserController {
    @Autowired
    public UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO addUser = userService.addUser(userDTO);
        return ResponseEntity.created(
                URI.create("api/users/"+addUser.getUserId())
        ).body(addUser);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserByEmailOrUserNameAndPassword(
            @PathVariable UUID userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam String password) {
        if ((email == null || email.isEmpty()) && (username == null || username.isEmpty())) {
            throw new IllegalArgumentException("Either email or username must be provided.");
        }

        List<UserDTO> users = userService.getUserByEmailOrUserNameAndPassword(userId, email, username, password);
        return ResponseEntity.ok((UserDTO) users);
    }

}

