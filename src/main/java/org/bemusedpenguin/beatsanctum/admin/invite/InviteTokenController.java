package org.bemusedpenguin.beatsanctum.admin.invite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/invites")
public class InviteTokenController {

    private final InviteTokenService inviteTokenService;

    public InviteTokenController(InviteTokenService inviteTokenService) {
        this.inviteTokenService = inviteTokenService;
    }

    @PostMapping
    public ResponseEntity<InviteTokenResponse> regenerateToken() {
        InviteToken token = inviteTokenService.regenerateToken();
        return ResponseEntity.ok(new InviteTokenResponse(token.getToken()));
    }
}