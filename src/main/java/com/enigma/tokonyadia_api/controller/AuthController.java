package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.AuthRequest;
import com.enigma.tokonyadia_api.dto.request.RegisterRequest;
import com.enigma.tokonyadia_api.dto.response.AuthResponse;
import com.enigma.tokonyadia_api.dto.response.RegisterResponse;
import com.enigma.tokonyadia_api.service.AuthService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestController
@RequestMapping(path = Constant.AUTH_API)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication, registration, and token management")
public class AuthController {
    @Value("${tokonyadia.refresh-token-expiration-in-hour}")
    private Integer REFRESH_TOKEN_EXPIRY;

    private final AuthService authService;

    @Operation(summary = "User login",
            description = "Authenticate a user with valid credentials. A refresh token will be set in cookies upon successful login.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid login credentials",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_AUTH_LOGIN_USER, authResponse);
    }

    @Operation(summary = "User registration",
            description = "Register a new user in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid registration data",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authService.register(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_REGISTER_USER, registerResponse);
    }

    @Operation(summary = "Refresh access token",
            description = "Generate a new access token using the refresh token stored in cookies.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid or missing refresh token",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_REFRESH_TOKEN, authResponse);
    }

    @Operation(summary = "User logout",
            description = "Logout the user by invalidating the current access token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(bearerToken);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.OK, null);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        try {
            Cookie cookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(Constant.REFRESH_TOKEN_COOKIE_NAME))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.REFRESH_TOKEN_REQUIRED));
            return cookie.getValue();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(Constant.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * REFRESH_TOKEN_EXPIRY);
        response.addCookie(cookie);
    }
}
