package dev.hgjtu.spring_auth_server;

import dev.hgjtu.spring_auth_server.dto.RegistrationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrationRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequestShouldPassValidation() {
        RegistrationRequest request = new RegistrationRequest(
                "john_doe",
                "abc12345", // валидный пароль: буквы + цифры, >=6 символов
                "john@example.com",
                List.of("USER")
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailIfUsernameBlank() {
        RegistrationRequest request = new RegistrationRequest(
                "", "abc12345", "john@example.com", List.of("USER")
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("username"));
    }

    @Test
    void shouldFailIfEmailInvalid() {
        RegistrationRequest request = new RegistrationRequest(
                "john_doe", "abc12345", "invalid-email", List.of("USER")
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("email"));
    }

    @Test
    void shouldFailIfPasswordTooShort() {
        RegistrationRequest request = new RegistrationRequest(
                "john_doe", "a1", "john@example.com", List.of("USER")
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("password"));
    }

    @Test
    void shouldFailIfPasswordMissingDigitOrLetter() {
        // только буквы
        RegistrationRequest request1 = new RegistrationRequest("john_doe", "abcdef", "john@example.com", List.of("USER"));
        Set<ConstraintViolation<RegistrationRequest>> violations1 = validator.validate(request1);
        assertThat(violations1).anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("password"));

        // только цифры
        RegistrationRequest request2 = new RegistrationRequest("john_doe", "123456", "john@example.com", List.of("USER"));
        Set<ConstraintViolation<RegistrationRequest>> violations2 = validator.validate(request2);
        assertThat(violations2).anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("password"));
    }
}

