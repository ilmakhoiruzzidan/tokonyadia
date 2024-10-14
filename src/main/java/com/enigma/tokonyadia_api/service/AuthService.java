package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.AuthRequest;
import com.enigma.tokonyadia_api.dto.response.LoginResponse;
import com.enigma.tokonyadia_api.dto.response.RegisterResponse;

public interface AuthService {
    LoginResponse login(AuthRequest request);
    RegisterResponse register(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
}
