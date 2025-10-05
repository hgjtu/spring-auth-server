package dev.hgjtu.spring_auth_server.config;

import dev.hgjtu.spring_auth_server.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UserConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        String defaultEncodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        DelegatingPasswordEncoder delegatingPasswordEncoder =
                new DelegatingPasswordEncoder(defaultEncodingId, encoders);

        return delegatingPasswordEncoder;
    }

    @Bean
    public CommandLineRunner initDefaultUsers(CustomUserDetailsService userDetailsService) {
        return args -> {
            userDetailsService.initDefaultUsers();
        };
    }
}
