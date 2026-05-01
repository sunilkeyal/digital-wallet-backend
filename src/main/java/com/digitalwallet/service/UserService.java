package com.digitalwallet.service;

import com.digitalwallet.dto.UserDto;
import com.digitalwallet.entity.User;
import com.digitalwallet.repository.UserRepository;
import com.digitalwallet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByOauth2ProviderAndSub(String provider, String sub) {
        return userRepository.findByOauth2ProviderAndOauth2Sub(provider, sub);
    }

    public UserDto createUserFromDto(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        // Handle roles - default to ROLE_USER if not provided
        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            String role = userDto.getRoles().iterator().next();
            user.setRoles(Set.of(role));
        } else {
            user.setRoles(Set.of("ROLE_USER"));
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return mapToDto(userRepository.save(user));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User createAdminUser() {
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);
        if (existingAdmin.isPresent()) {
            User admin = existingAdmin.get();
            if (admin.getPassword() == null) {
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(admin);
            }
            return admin;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRoles(Set.of("ROLE_ADMIN"));
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(admin);
    }

    public boolean validatePassword(User user, String rawPassword) {
        if (user.getPassword() == null) return false;
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public UserDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public UserDto updateUser(String id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setUpdatedAt(LocalDateTime.now());
        return mapToDto(userRepository.save(user));
    }

    public UserDto assignRole(String userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().add("ROLE_" + role.toUpperCase());
        user.setUpdatedAt(LocalDateTime.now());
        return mapToDto(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRoles());
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setRoles(user.getRoles());
        dto.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        dto.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);
        return dto;
    }
}
