package com.digitalwallet.controller;

import com.digitalwallet.dto.UserDto;
import com.digitalwallet.entity.User;
import com.digitalwallet.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!userService.validatePassword(user, password)) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = userService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token, "userId", user.getId()));
    }

    @PostMapping("/oauth2/callback")
    public ResponseEntity<Map<String, String>> oauth2Callback(@RequestBody Map<String, String> oauthRequest) {
        String provider = oauthRequest.get("provider");
        String sub = oauthRequest.get("sub");
        String email = oauthRequest.get("email");
        String firstName = oauthRequest.get("firstName");
        String lastName = oauthRequest.get("lastName");

        User user = userService.findByOauth2ProviderAndSub(provider, sub)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setOauth2Provider(provider);
                    newUser.setOauth2Sub(sub);
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setRoles(Set.of("ROLE_USER"));
                    return userService.createUser(newUser);
                });

        String token = userService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token, "userId", user.getId()));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        String userId = userService.getCurrentUserId();
        System.out.println("getCurrentUser called, userId: " + userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/debug")
    public ResponseEntity<?> debug() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Debug - Principal: " + auth.getPrincipal());
        System.out.println("Debug - Authorities: " + auth.getAuthorities());
        return ResponseEntity.ok(Map.of(
            "principal", auth.getPrincipal(),
            "authorities", auth.getAuthorities().toString(),
            "isAuthenticated", auth.isAuthenticated()
        ));
    }
}
