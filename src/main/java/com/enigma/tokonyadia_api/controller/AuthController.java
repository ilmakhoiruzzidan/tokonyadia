package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.AuthRequest;
import com.enigma.tokonyadia_api.dto.response.LoginResponse;
import com.enigma.tokonyadia_api.dto.response.RegisterResponse;
import com.enigma.tokonyadia_api.service.AuthService;
import com.enigma.tokonyadia_api.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_AUTH_LOGIN_USER, loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request){
        RegisterResponse registerResponse = authService.register(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_REGISTER_USER, registerResponse);
    }

}
