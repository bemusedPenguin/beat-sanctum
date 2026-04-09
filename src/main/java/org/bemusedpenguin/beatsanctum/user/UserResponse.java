package org.bemusedpenguin.beatsanctum.user;

import java.time.LocalDateTime;

public record UserResponse(Long id, String username, boolean admin, LocalDateTime createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.isAdmin(),
                user.getCreatedAt()
        );
    }
}
