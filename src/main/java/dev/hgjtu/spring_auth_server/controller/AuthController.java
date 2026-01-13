package dev.hgjtu.spring_auth_server.controller;

import dev.hgjtu.spring_auth_server.dto.ErrorResponse;
import dev.hgjtu.spring_auth_server.dto.RegistrationRequest;
import dev.hgjtu.spring_auth_server.dto.RegistrationResponse;
import dev.hgjtu.spring_auth_server.model.UserCredentials;
import dev.hgjtu.spring_auth_server.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
