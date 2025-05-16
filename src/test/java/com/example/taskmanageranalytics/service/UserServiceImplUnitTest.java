package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.dto.user.UserRequest;
import com.example.taskmanageranalytics.dto.user.UserResponse;
import com.example.taskmanageranalytics.entity.User;
import com.example.taskmanageranalytics.exception.user.UserAlreadyExistsException;
import com.example.taskmanageranalytics.exception.user.UserNotFoundException;
import com.example.taskmanageranalytics.repository.UserRepository;
import com.example.taskmanageranalytics.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private final User testUser = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("encodedPassword")
            .role(User.Role.USER)
            .build();

    @Test
    void createUser_ShouldReturnUserResponse_WhenUserDoesNotExist() {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(User.Role.USER, response.getRole());

        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameExists() {

        UserRequest request = new UserRequest();
        request.setUsername("existinguser");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserResponse_WhenUserExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void updateUser_ShouldUpdateUsername_WhenNewUsernameIsUnique() {

        UserRequest request = new UserRequest();
        request.setUsername("newusername");
        request.setEmail("test@example.com");

        User existingUser = User.builder()
                .id(1L)
                .username("oldusername")
                .email("test@example.com")
                .password("encodedPassword")
                .role(User.Role.USER)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newusername")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(1L, request);

        assertEquals("newusername", response.getUsername());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("newusername") &&
                        user.getEmail().equals("test@example.com")
        ));
    }

    @Test
    void updateUser_ShouldUpdatePassword_WhenNewPasswordProvided() {

        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.updateUser(1L, request);

        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(argThat(user ->
                user.getPassword().equals("newEncodedPassword")
        ));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(99L));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username("user2").build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserResponse> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    void getRawUserById_ShouldReturnUser_WhenUserExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User user = userService.getRawUserById(1L);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }
}