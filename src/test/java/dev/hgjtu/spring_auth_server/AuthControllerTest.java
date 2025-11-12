package dev.hgjtu.spring_auth_server;

import dev.hgjtu.spring_auth_server.controller.AuthController;
import dev.hgjtu.spring_auth_server.dto.ErrorResponse;
import dev.hgjtu.spring_auth_server.dto.RegistrationRequest;
import dev.hgjtu.spring_auth_server.dto.RegistrationResponse;
import dev.hgjtu.spring_auth_server.model.UserCredentials;
import dev.hgjtu.spring_auth_server.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private CustomUserDetailsService userDetailsService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        controller = new AuthController();
        controller.getClass().getDeclaredFields();
        controller = new AuthController();
        ReflectionTestUtils.setField(controller, "userDetailsService", userDetailsService);

    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegistrationRequest request = new RegistrationRequest(
                "john_doe", "pass123", "john@example.com", List.of("ADMIN")
        );

        UserCredentials mockUser = new UserCredentials(
                1L, "john_doe", "encodedPass", "john@example.com", List.of("ADMIN")
        );

        when(userDetailsService.createUserCredentials(
                anyString(), anyString(), anyString(), anyList()
        )).thenReturn(mockUser);

        ResponseEntity<?> response = controller.registerUser(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(RegistrationResponse.class);

        RegistrationResponse body = (RegistrationResponse) response.getBody();
        assertThat(body.getUsername()).isEqualTo("john_doe");
        assertThat(body.getEmail()).isEqualTo("john@example.com");
        assertThat(body.getRoles()).containsExactly("ADMIN");
    }

    @Test
    void shouldAssignDefaultRoleIfNoneProvided() {
        RegistrationRequest request = new RegistrationRequest(
                "jane_doe", "password123", "jane@example.com", null
        );

        UserCredentials mockUser = new UserCredentials(
                2L, "jane_doe", "encodedPass", "jane@example.com", List.of("USER")
        );

        when(userDetailsService.createUserCredentials(
                anyString(), anyString(), anyString(), anyList()
        )).thenReturn(mockUser);

        ResponseEntity<?> response = controller.registerUser(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        RegistrationResponse body = (RegistrationResponse) response.getBody();
        assertThat(body.getRoles()).containsExactly("USER");
    }

    @Test
    void shouldReturnErrorResponseWhenExceptionThrown() {
        RegistrationRequest request = new RegistrationRequest(
                "bad_user", "weak", "bad@example.com", List.of("USER")
        );

        when(userDetailsService.createUserCredentials(
                anyString(), anyString(), anyString(), anyList()
        )).thenThrow(new RuntimeException("User already exists"));

        ResponseEntity<?> response = controller.registerUser(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertThat(error.getMessage()).isEqualTo("User already exists");
    }
}
