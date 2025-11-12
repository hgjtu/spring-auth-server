package dev.hgjtu.spring_auth_server;

import dev.hgjtu.spring_auth_server.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private final SecurityConfig config = new SecurityConfig();

    @Test
    void shouldCreateDefaultFilterChainWithoutException() throws Exception {
        HttpSecurity mockHttp = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        SecurityFilterChain chain = config.defaultFilterChain(mockHttp);

        assertThat(chain).isNotNull();
    }

    @Test
    void shouldReturnRequestCache() {
        assertThat(config.requestCache()).isNotNull();
    }
}

