package dev.hgjtu.spring_auth_server.service;

import dev.hgjtu.spring_auth_server.model.UserCredentials;
import dev.hgjtu.spring_auth_server.repos.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialsRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username)
                );
    }

    public UserCredentials createUserCredentials(String username, String password,
                                                 List<String> roles) {
        if (userCredentialsRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists: " + username);
        }

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(username);
        userCredentials.setPassword(passwordEncoder.encode(password));
        userCredentials.setRoles(roles);

        return userCredentialsRepository.save(userCredentials);
    }

    public void initDefaultUsers() {
        if (!userCredentialsRepository.existsByUsername("admin")) {
            createUserCredentials("admin", "1234", List.of("USER", "ADMIN"));
        }
    }
}
