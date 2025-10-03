package dev.hgjtu.spring_auth_server.impl;

import dev.hgjtu.spring_auth_server.entity.CustomRegisteredClient;
import dev.hgjtu.spring_auth_server.entity.CustomRegisteredClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {
    private final CustomRegisteredClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {}

    @Override
    public RegisteredClient findById(String id) {
        return mapToClient(clientRepository.findById(Long.valueOf(id)).orElseThrow());
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return mapToClient(clientRepository.findByClientId(clientId).orElseThrow());
    }

    public CustomRegisteredClient save(CustomRegisteredClient client) {
        return clientRepository.save(client);
    }

    public List<CustomRegisteredClient> findAll() {
        return clientRepository.findAll();
    }

    private RegisteredClient mapToClient(CustomRegisteredClient client) {
        var type = client.getGrantType().equals("client_credentials") ?
                AuthorizationGrantType.CLIENT_CREDENTIALS : AuthorizationGrantType.AUTHORIZATION_CODE;
        var tokenFormat = client.getTokenFormat().equals("reference") ?
                OAuth2TokenFormat.REFERENCE : OAuth2TokenFormat.SELF_CONTAINED;
        var clientSettings = client.isRequireProofKey() ?
                ClientSettings.builder().requireProofKey(true).build() :
                ClientSettings.builder().requireProofKey(false).build();

        return RegisteredClient
                .withId(client.getId())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectUri())
                .authorizationGrantType(type)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(tokenFormat)
                        .accessTokenTimeToLive(Duration.ofHours(12))
                        .build()
                )
                .clientSettings(clientSettings)
                .scope("openid")
                .build();
    }
}
