package dev.hgjtu.spring_auth_server.entity;

import dev.hgjtu.spring_auth_server.impl.RegisteredClientRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisteredClientService {
    private final RegisteredClientRepositoryImpl clientRepository;

    public CustomRegisteredClient registerClient(CustomRegisteredClient client) {
        return clientRepository.save(client);
    }

    public List<CustomRegisteredClient> getAllClients() {
        return clientRepository.findAll();
    }
}
