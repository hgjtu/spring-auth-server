package dev.hgjtu.spring_auth_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class AppConfig implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    //TODO это что вообще
    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("hgjtu")
            .password("1234")
            .roles("USER", "ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(String.valueOf(UUID.randomUUID()))
            .clientId("client")
            .clientSecret("secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:9091/oauth/token")
            .scope("openid")
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofHours(6))
                    .build()
            )
            .clientSettings(
                ClientSettings.builder()
                    .requireProofKey(false)
                    .build()
            )
            .build();

        RegisteredClient registeredClient1 = RegisteredClient.withId(String.valueOf(UUID.randomUUID()))
            .clientId("client_1")
            .clientSecret("secret_1")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:9092/oauth/token")
            .scope("openid")
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofHours(6))
                    .build()
            )
            .clientSettings(
                ClientSettings.builder()
                    .requireProofKey(true)
                    .build()
            )
            .build();

        return new InMemoryRegisteredClientRepository(registeredClient, registeredClient1);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build(); //TODO это минимум, что-то нажо добавить (эндпоинт?)
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

    @Override
    public void run(String... args) throws Exception {
        byte[] code = new byte[32];
        new SecureRandom().nextBytes(code);
        String verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(code);

        byte[] digestedVerifier = MessageDigest
                .getInstance("SHA-256").digest(verifier.getBytes());
        String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digestedVerifier);

        logger.info("Challenge Verifier: " + verifier); //TODO а на кой
        logger.info("Code Challenge: " + codeChallenge);
    }
}
