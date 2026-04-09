package org.bemusedpenguin.beatsanctum.admin.invite;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class InviteTokenService {

    private final InviteTokenRepository inviteTokenRepository;

    public InviteTokenService(InviteTokenRepository inviteTokenRepository) {
        this.inviteTokenRepository = inviteTokenRepository;
    }

    @Transactional
    public InviteToken regenerateToken() {
        inviteTokenRepository.deleteAll();
        return inviteTokenRepository.save(new InviteToken(UUID.randomUUID().toString()));
    }

    public void validate(String token) {
        InviteToken invite = inviteTokenRepository.findFirstBy()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No invite token exists"));

        if (!invite.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid invite token");
        }
    }
}