package org.bemusedpenguin.beatsanctum.admin.invite;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invite_tokens")
public class InviteToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected InviteToken() {}

    public InviteToken(String token) {
        this.token = token;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
