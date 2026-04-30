package com.digitalwallet.service;

import com.digitalwallet.dto.UserDto;
import com.digitalwallet.entity.User;
import com.digitalwallet.repository.UserRepository;
import com.digitalwallet.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user123");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setRoles(Set.of("ROLE_USER"));
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void findByEmail_returnsUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmail_returnsEmptyWhenNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail("notfound@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void createUser_savesAndReturnsUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUser);

        assertEquals("user123", result.getId());
        verify(userRepository).save(testUser);
    }

    @Test
    void getUserById_returnsUserDto() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));

        UserDto result = userService.getUserById("user123");

        assertEquals("user123", result.getId());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getAllUsers_returnsListOfDtos() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<UserDto> results = userService.getAllUsers();

        assertEquals(1, results.size());
        assertEquals("test@example.com", results.get(0).getEmail());
    }

    @Test
    void generateToken_returnsToken() {
        when(jwtUtil.generateToken("user123", "test@example.com", Set.of("ROLE_USER")))
                .thenReturn("fake-token");

        String token = userService.generateToken(testUser);

        assertEquals("fake-token", token);
        verify(jwtUtil).generateToken("user123", "test@example.com", Set.of("ROLE_USER"));
    }

    @Test
    void assignRole_addsRoleToUser() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDto result = userService.assignRole("user123", "admin");

        assertTrue(testUser.getRoles().contains("ROLE_ADMIN"));
    }

    @Test
    void deleteUser_deletesUser() {
        userService.deleteUser("user123");

        verify(userRepository).deleteById("user123");
    }
}
