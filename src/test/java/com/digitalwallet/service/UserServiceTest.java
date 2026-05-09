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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "adminUsername", "admin");
        ReflectionTestUtils.setField(userService, "adminPassword", "adminpass");
        ReflectionTestUtils.setField(userService, "adminEmail", "admin@example.com");
    }

    @Test
    void createUserFromDto_encodesPasswordAndSetsRole() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setFirstName("Test");
        dto.setLastName("User");

        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId("new-id");
            return saved;
        });

        UserDto result = userService.createUserFromDto(dto);

        assertEquals("new-id", result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertFalse(result.getRoles().isEmpty());
    }

    @Test
    void validatePassword_returnsTrueWhenMatches() {
        User user = new User();
        user.setPassword("encoded");
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);

        assertEquals(true, userService.validatePassword(user, "password"));
    }

    @Test
    void validatePassword_returnsFalseWhenNoPassword() {
        User user = new User();
        user.setPassword(null);

        assertFalse(userService.validatePassword(user, "password"));
    }

    @Test
    void getUserById_throwsWhenNotFound() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById("missing"));
    }

    @Test
    void assignRole_addsRoleAndReturnsDto() {
        User user = new User();
        user.setId("user1");
        user.setRoles(new HashSet<>(Set.of("ROLE_USER")));

        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDto dto = userService.assignRole("user1", "admin");

        assertEquals(true, dto.getRoles().contains("ROLE_ADMIN"));
    }

    @Test
    void updateUser_updatesAndReturnsDto() {
        User existing = new User();
        existing.setId("user1");
        existing.setFirstName("Old");
        existing.setLastName("Name");

        UserDto updateDto = new UserDto();
        updateDto.setFirstName("New");
        updateDto.setLastName("Name");
        updateDto.setPhoneNumber("555-1234");
        updateDto.setDateOfBirth("2000-01-01");

        when(userRepository.findById("user1")).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        UserDto result = userService.updateUser("user1", updateDto);

        assertEquals("New", result.getFirstName());
        assertEquals("555-1234", result.getPhoneNumber());
    }

    @Test
    void deleteUser_delegatesToRepository() {
        userService.deleteUser("user1");

        verify(userRepository).deleteById("user1");
    }

    @Test
    void generateToken_delegatesToJwtUtil() {
        User user = new User();
        user.setId("user1");
        user.setEmail("user@example.com");
        user.setRoles(Set.of("ROLE_USER"));

        when(jwtUtil.generateToken("user1", "user@example.com", Set.of("ROLE_USER"))).thenReturn("jwt-token");

        assertEquals("jwt-token", userService.generateToken(user));
    }

    @Test
    void createAdminUser_returnsExistingAdminWhenPresent() {
        User existing = new User();
        existing.setEmail("admin@example.com");
        existing.setPassword("encoded");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existing));

        User result = userService.createAdminUser();

        assertEquals(existing, result);
    }

    @Test
    void createAdminUser_createsNewAdminWhenMissing() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("adminpass")).thenReturn("encoded-admin");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createAdminUser();

        assertEquals("admin@example.com", result.getEmail());
        assertTrue(result.getRoles().contains("ROLE_ADMIN"));
        assertEquals("encoded-admin", result.getPassword());
    }

    @Test
    void getAllUsers_returnsMappedDtos() {
        User user = new User();
        user.setId("user1");
        user.setEmail("user@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void findByEmail_returnsOptional() {
        User user = new User();
        user.setEmail("found@example.com");
        when(userRepository.findByEmail("found@example.com")).thenReturn(Optional.of(user));

        assertTrue(userService.findByEmail("found@example.com").isPresent());
    }

    @Test
    void findByOauth2ProviderAndSub_returnsOptional() {
        User user = new User();
        when(userRepository.findByOauth2ProviderAndOauth2Sub("google", "sub123")).thenReturn(Optional.of(user));

        assertTrue(userService.findByOauth2ProviderAndSub("google", "sub123").isPresent());
    }
}
