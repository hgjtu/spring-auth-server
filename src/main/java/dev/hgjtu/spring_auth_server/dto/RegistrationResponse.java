package dev.hgjtu.spring_auth_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RegistrationResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
