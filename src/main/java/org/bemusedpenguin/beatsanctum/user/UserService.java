package org.bemusedpenguin.beatsanctum.user;

import org.bemusedpenguin.beatsanctum.admin.invite.InviteTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final InviteTokenService inviteTokenService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       InviteTokenService inviteTokenService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.inviteTokenService = inviteTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword, String inviteToken) {
        inviteTokenService.validate(inviteToken);

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        User user = new User(username, passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}