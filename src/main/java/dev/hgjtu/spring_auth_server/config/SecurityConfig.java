package dev.hgjtu.spring_auth_server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

//        http.with(OAuth2AuthorizationServerConfigurer.authorizationServer(),
//                Customizer.withDefaults());

        http
            .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint(
                            new LoginUrlAuthenticationEntryPoint("/login")
                    )
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(oidc -> oidc
                        .providerConfigurationEndpoint(provider -> provider
                                .providerConfigurationCustomizer(customizer -> {
                                    // Настраиваем OIDC provider configuration
                                })
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .requestCache(cache -> cache
                    .requestCache(requestCache())
            )
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/auth/**", "/login",
                            "/error",
                            "/.well-known/appspecific/com.chrome.devtools.json").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache() {
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                String requestUrl = request.getRequestURL().toString();
                if (requestUrl.contains("/oauth2/authorize")) {
                    super.saveRequest(request, response);
                }
            }
        };
    }
}
