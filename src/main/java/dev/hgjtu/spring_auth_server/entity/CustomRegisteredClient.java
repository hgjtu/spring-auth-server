package dev.hgjtu.spring_auth_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "clients")
public class CustomRegisteredClient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String clientId;

    private String clientSecret;

    private String grantType;

    @Column(nullable = false)
    private String redirectUri;

    @Column(unique = true)
    private String clientName;

    private boolean requireProofKey;

    private String tokenFormat;
}
