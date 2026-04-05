//package dev.hgjtu.spring_auth_server;
//
//import dev.hgjtu.spring_auth_server.config.UserConfig;
//import dev.hgjtu.spring_auth_server.service.CustomUserDetailsService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class UserConfigTest {
//
//    private final UserConfig config = new UserConfig();
//
//    @Test
//    void shouldReturnValidPasswordEncoder() {
//        PasswordEncoder encoder = config.passwordEncoder();
//        assertThat(encoder).isNotNull();
//
//        String raw = "1234";
//        String encoded = encoder.encode(raw);
//
//        assertThat(encoder.matches(raw, encoded)).isTrue();
//    }
//
//    @Test
//    void initDefaultUsers_shouldInvokeService() throws Exception {
//        CustomUserDetailsService service = mock(CustomUserDetailsService.class);
//        CommandLineRunner runner = config.initDefaultUsers(service);
//
//        runner.run();
//
//        verify(service, times(1)).initDefaultUsers();
//    }
//}
//
