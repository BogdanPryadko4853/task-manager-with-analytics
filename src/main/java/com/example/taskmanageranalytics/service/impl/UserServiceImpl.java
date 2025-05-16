package com.example.taskmanageranalytics.service.impl;

import com.example.taskmanageranalytics.dto.UserRequest;
import com.example.taskmanageranalytics.dto.UserResponse;
import com.example.taskmanageranalytics.entity.User;
import com.example.taskmanageranalytics.exception.UserAlreadyExistsException;
import com.example.taskmanageranalytics.exception.UserNotFoundException;
import com.example.taskmanageranalytics.repository.UserRepository;
import com.example.taskmanageranalytics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UserAlreadyExistsException("username", userRequest.getUsername());
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException("email", userRequest.getEmail());
        }

        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .role(User.Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getUsername().equals(userRequest.getUsername())) {
            if (userRepository.existsByUsername(userRequest.getUsername())) {
                throw new UserAlreadyExistsException("username", userRequest.getUsername());
            }
            user.setUsername(userRequest.getUsername());
        }

        if (!user.getEmail().equals(userRequest.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new UserAlreadyExistsException("email", userRequest.getEmail());
            }
            user.setEmail(userRequest.getEmail());
        }

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getRawUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdTasksCount(user.getCreatedTasks() != null ? user.getCreatedTasks().size() : 0)
                .assignedTasksCount(user.getAssignedTasks() != null ? user.getAssignedTasks().size() : 0)
                .build();
    }
}