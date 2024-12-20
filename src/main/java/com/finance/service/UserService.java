package com.finance.service;

import com.finance.dto.UserDTO;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) throws InvalidUserException {
        User existingUser = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        existingUser.setUserName(userDTO.getUserName());
        existingUser.setEmail(userDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public void deleteUser(UUID userId) throws InvalidUserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserException("User not Found"));

        userRepository.delete(user);
    }

    public List<UserDTO> getuserById(UUID userId, String userName, String firstName, String lastName, String email) {
        return List.of();
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public class InvalidUserException extends Exception {
        public InvalidUserException(String userNotFound) {
            super(userNotFound);
        }
    }

    public List<UserDTO> getUserByEmailOrUserNameAndPassword(UUID userId, String email, String username, String password) {
        // Fetch the user by userId
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User  not found"));

        if ((user.getEmail().equals(email) || user.getUserName().equals(username)) && user.getPassword().equals(password)) {
            return Collections.singletonList(convertToDto(user));
        } else {
            return Collections.emptyList();
        }
    }

    private UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setPassword(userDTO.getPassword());
        userDTO.setFirstName(userDTO.getFirstName());
        userDTO.setLastName(userDTO.getLastName());
        userDTO.setEmail(userDTO.getEmail());
        userDTO.setRole(userDTO.getRole());
        userDTO.setCreatedAt(userDTO.getCreatedAt());
        return userDTO;
    }
}
