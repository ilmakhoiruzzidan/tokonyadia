package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.AuthRequest;
import com.enigma.tokonyadia_api.dto.request.RegisterRequest;
import com.enigma.tokonyadia_api.dto.response.AuthResponse;
import com.enigma.tokonyadia_api.dto.response.RegisterResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    RegisterResponse register(RegisterRequest request);
    AuthResponse refreshToken(String token);
    void logout(String accessToken);
}
