package dev.hgjtu.spring_auth_server;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dev.hgjtu.spring_auth_server.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {

    private final AppConfig config = new AppConfig();

    @Test
    void shouldCreateRegisteredClientRepositoryWithExpectedClient() {
        RegisteredClientRepository repo = config.registeredClientRepository();

        RegisteredClient client = repo.findByClientId("web-client");

        assertThat(client).isNotNull();
        assertThat(client.getClientSecret()).contains("secret");
        assertThat(client.getScopes()).contains("openid", "profile", "read");
        assertThat(client.getClientSettings().isRequireProofKey()).isTrue();
        assertThat(client.getTokenSettings().getAccessTokenTimeToLive()).isNotNull();
    }

    @Test
    void shouldCreateAuthorizationServerSettings() {
        AuthorizationServerSettings settings = config.authorizationServerSettings();
        assertThat(settings).isNotNull();
    }

    @Test
    void shouldCreateJwkSourceAndJwtDecoder() throws Exception {
        JWKSource<SecurityContext> jwkSource = config.jwkSource();
        assertThat(jwkSource).isNotNull();

        JwtDecoder jwtDecoder = config.jwtDecoder(jwkSource);
        assertThat(jwtDecoder).isNotNull();
    }
}

