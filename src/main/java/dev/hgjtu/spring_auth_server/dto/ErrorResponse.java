package dev.hgjtu.spring_auth_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Map<String, String> details;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
