package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    boolean validateToken(String token);
    String getUserIdFromToken(String token);
}
