package org.bemusedpenguin.beatsanctum.auth;

import org.bemusedpenguin.beatsanctum.user.User;
import org.bemusedpenguin.beatsanctum.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(SignupRequest request) {
        User user = userService.register(request.username(), request.password(), request.inviteToken());
        return new AuthResponse(jwtService.generateToken(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userService.findByUsername(request.username());
        if (!userService.checkPassword(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return new AuthResponse(jwtService.generateToken(user));
    }
}