package dev.hgjtu.spring_auth_server.service;

import dev.hgjtu.spring_auth_server.dto.ChangePasswordRequest;
import dev.hgjtu.spring_auth_server.model.UserCredentials;
import dev.hgjtu.spring_auth_server.repos.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserCredentialsRepository userCredentialsRepository, PasswordEncoder passwordEncoder) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialsRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username)
                );
    }

    public UserCredentials createUserCredentials(String username, String password, String email,
                                                 List<String> roles) {
        if (userCredentialsRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists: " + username);
        }
        if (email != null && userCredentialsRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered: " + email);
        }

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(username);
        userCredentials.setPassword(passwordEncoder.encode(password));
        userCredentials.setEmail(email);
        userCredentials.setRoles(roles != null && !roles.isEmpty() ? roles : List.of("USER"));

        return userCredentialsRepository.save(userCredentials);
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        UserCredentials userCredentials = userCredentialsRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), userCredentials.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), userCredentials.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        userCredentials.setPassword(passwordEncoder.encode(request.getNewPassword()));
        log.info("Password changed successfully for user: {}", userCredentials.getUsername());
        userCredentialsRepository.save(userCredentials);
    }

    public void changeEmail(String username, String newEmail) {
        UserCredentials userCredentials = userCredentialsRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (newEmail.equals(userCredentials.getEmail())) {
            throw new IllegalArgumentException("New email must be different from current email");
        }

        userCredentials.setEmail(newEmail);
        log.info("Email changed successfully for user: {}", userCredentials.getUsername());
        userCredentialsRepository.save(userCredentials);
    }

    public void initDefaultUsers() {
        if (!userCredentialsRepository.existsByUsername("admin")) {
            createUserCredentials("admin", "1234", "admin@example.com", List.of("USER", "ADMIN"));
        }
    }
}
