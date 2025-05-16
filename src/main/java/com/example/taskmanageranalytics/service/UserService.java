package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.dto.user.UserRequest;
import com.example.taskmanageranalytics.dto.user.UserResponse;
import com.example.taskmanageranalytics.entity.User;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    UserResponse getUserById(Long id);

    UserResponse getUserByUsername(String username);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserRequest userRequest);

    void deleteUser(Long id);

    User getRawUserById(Long id);
}
