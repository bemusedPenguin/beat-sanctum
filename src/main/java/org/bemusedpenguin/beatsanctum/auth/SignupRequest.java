package org.bemusedpenguin.beatsanctum.auth;

public record SignupRequest(String username, String password, String inviteToken) {
}
