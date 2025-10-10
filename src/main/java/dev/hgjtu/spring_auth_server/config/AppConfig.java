package dev.hgjtu.spring_auth_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class AppConfig {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // PKCE
        RegisteredClient registeredClient = RegisteredClient.withId(String.valueOf(UUID.randomUUID()))
            .clientId("web-client")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:5050/login/oauth2/code/web-client")
            .scope("read")
            .scope("openid")
            .scope("profile")
            .tokenSettings(
                TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(12))
                        .refreshTokenTimeToLive(Duration.ofDays(4))
                        .build()
            )
            .clientSettings(
                ClientSettings.builder()
                    .requireProofKey(true)
                    .requireAuthorizationConsent(false)
                    .build()
            )
            .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder() //TODO вроде может быть что-то еще
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(String.valueOf(UUID.randomUUID()))
                .build();

        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
}
