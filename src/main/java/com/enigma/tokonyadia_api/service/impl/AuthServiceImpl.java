package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.util.MapperUtil;
import com.enigma.tokonyadia_api.dto.request.AuthRequest;
import com.enigma.tokonyadia_api.dto.request.RegisterRequest;
import com.enigma.tokonyadia_api.dto.response.AuthResponse;
import com.enigma.tokonyadia_api.dto.response.RegisterResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Seller;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.service.*;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final CustomerService customerService;
    private final SellerService sellerService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(AuthRequest request) {
        validationUtil.validate(request);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        String refreshToken = refreshTokenService.createToken(userAccount.getId());
        String accessToken = jwtService.generateAccessToken(userAccount);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(userAccount.getRole().getDescription())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(RegisterRequest request) {
        try {
            validationUtil.validate(request);
            if (request.getRole().equalsIgnoreCase("ADMIN")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ROLE_NOT_FOUND);
            } else if (request.getRole().equalsIgnoreCase("PELANGGAN")) {
                UserAccount userAccount = UserAccount.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(UserRole.ROLE_CUSTOMER)
                        .build();
                userService.create(userAccount);
                Customer customer = Customer.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .userAccount(userAccount)
                        .build();
                customerService.create(customer);
                return MapperUtil.toRegisterResponse(userAccount);
            } else if (request.getRole().equalsIgnoreCase("PENJUAL")) {
                UserAccount userAccount = UserAccount.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(UserRole.ROLE_SELLER)
                        .build();
                userService.create(userAccount);
                Seller seller = Seller.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .userAccount(userAccount)
                        .build();
                sellerService.create(seller);
                return MapperUtil.toRegisterResponse(userAccount);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ROLE_NOT_FOUND);
    }

    @Override
    public AuthResponse refreshToken(String token) {
        String userId = refreshTokenService.getUserIdByToken(token);
        UserAccount userAccount = userService.getById(userId);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(userId);
        String newToken = jwtService.generateAccessToken(userAccount);
        return AuthResponse.builder()
                .accessToken(newToken)
                .refreshToken(newRefreshToken)
                .role(userAccount.getRole().getDescription())
                .build();
    }

    @Override
    public void logout(String accessToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        refreshTokenService.deleteRefreshToken(userAccount.getId());
        jwtService.blacklistAccessToken(accessToken);
    }



}
