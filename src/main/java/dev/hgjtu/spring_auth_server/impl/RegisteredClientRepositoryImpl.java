//package dev.hgjtu.spring_auth_server.impl;
//
//import dev.hgjtu.spring_auth_server.entity.CustomRegisteredClient;
//import dev.hgjtu.spring_auth_server.entity.CustomRegisteredClientRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
//import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
//import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {
//    private final CustomRegisteredClientRepository clientRepository;
//
//    @Override
//    public void save(RegisteredClient registeredClient) {}
//
//    @Override
//    public RegisteredClient findById(String id) {
//        return mapToClient(clientRepository.findById(Long.valueOf(id)).orElseThrow());
//    }
//
//    @Override
//    public RegisteredClient findByClientId(String clientId) {
//        return mapToClient(clientRepository.findByClientId(clientId).orElseThrow());
//    }
//
//    public CustomRegisteredClient save(CustomRegisteredClient client) {
//        return clientRepository.save(client);
//    }
//
//    private RegisteredClient mapToClient(CustomRegisteredClient client) {
//        List<AuthorizationGrantType> grantTypes = new ArrayList<>();
//
//        if ("authorization_code".equals(client.getGrantType())) {
//            grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//        } else if ("client_credentials".equals(client.getGrantType())) {
//            grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
//        } else if ("refresh_token".equals(client.getGrantType())) {
//            grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
//        }
//
//        if ("authorization_code".equals(client.getGrantType())) {
//            grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
//        }
//
//        var tokenFormat = "reference".equals(client.getTokenFormat()) ?
//                OAuth2TokenFormat.REFERENCE : OAuth2TokenFormat.SELF_CONTAINED;
//
//        var clientSettings = client.isRequireProofKey() ?
//                ClientSettings.builder().requireProofKey(true).build() :
//                ClientSettings.builder().requireProofKey(false).build();
//
//        RegisteredClient.Builder builder = RegisteredClient
//                .withId(client.getId())
//                .clientId(client.getClientId())
//                .clientSecret(client.getClientSecret())
//                .redirectUri(client.getRedirectUri())
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .tokenSettings(TokenSettings.builder()
//                        .accessTokenFormat(tokenFormat)
//                        .accessTokenTimeToLive(Duration.ofHours(12))
//                        .build()
//                )
//                .clientSettings(clientSettings)
//                .scope("openid");
//
//        grantTypes.forEach(builder::authorizationGrantType);
//
//        return builder.build();
//    }
//}
