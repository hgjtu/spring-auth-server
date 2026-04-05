package dev.hgjtu.spring_auth_server.controller;

import dev.hgjtu.spring_auth_server.dto.ChangePasswordRequest;
import dev.hgjtu.spring_auth_server.dto.ErrorResponse;
import dev.hgjtu.spring_auth_server.dto.RegistrationRequest;
import dev.hgjtu.spring_auth_server.dto.RegistrationResponse;
import dev.hgjtu.spring_auth_server.model.UserCredentials;
import dev.hgjtu.spring_auth_server.service.CustomUserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            List<String> roles = registrationRequest.getRoles() != null && !registrationRequest.getRoles().isEmpty()
                    ? registrationRequest.getRoles()
                    : List.of("USER");

            UserCredentials user = userDetailsService.createUserCredentials(
                    registrationRequest.getUsername(),
                    registrationRequest.getPassword(),
                    registrationRequest.getEmail(),
                    roles
            );

            return ResponseEntity.ok().body(
                    new RegistrationResponse(
                            user.getId(), user.getUsername(),
                            user.getEmail(), user.getRoles()
                    )
            );

        } catch (RuntimeException e) {
//            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(e.getMessage())
            );
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication,
                                            @RequestBody ChangePasswordRequest request) {
        try {
            userDetailsService.changePassword(authentication.getName(), request);
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(Authentication authentication,
                                            @RequestBody String newEmail) {
        try {
            userDetailsService.changeEmail(authentication.getName(), newEmail);
            return ResponseEntity.ok(Map.of("message", "Email changed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
