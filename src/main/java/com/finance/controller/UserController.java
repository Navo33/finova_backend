package com.finance.controller;

import com.finance.dto.UserDTO;
import com.finance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PutMapping("/{userId}")
    @Operation(summary = "Update an existing user", description = "Updates an exting user in the system")
    public <InvalidUserException> ResponseEntity<UserDTO> updateUser(
          @PathVariable UUID userId,
          @Valid @RequestBody UserDTO userDTO
    ) {
        try {
            userDTO.setUserId(userId);
            UserDTO updatedUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (UserService.InvalidUserException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Delete user by it's Id")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable UUID userId) throws UserService.InvalidUserException {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user by Id", description = "Retrives uses by Id from the system")
    public ResponseEntity<List<UserDTO>> getUserById(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "jack@123") String userName,
            @RequestParam(defaultValue = "Jack") String firstName,
            @RequestParam(defaultValue = "David") String lastName,
            @RequestParam(defaultValue = "jack@gmail.com") String email
    ) {
        List<UserDTO> users = userService.getuserById(userId, userName, firstName, lastName, email);
        return ResponseEntity.ok(users);
    }
}

