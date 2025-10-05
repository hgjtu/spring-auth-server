package dev.hgjtu.spring_auth_server.config;

import dev.hgjtu.spring_auth_server.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //NoOpPasswordEncoder.getInstance()
    }

    @Bean
    public CommandLineRunner initDefaultUsers(CustomUserDetailsService userDetailsService) {
        return args -> {
            userDetailsService.initDefaultUsers();
        };
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("admin")
//                .password("1234")
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
}
