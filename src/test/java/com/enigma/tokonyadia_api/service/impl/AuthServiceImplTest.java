package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.dto.request.AuthRequest;
import com.enigma.tokonyadia_api.dto.request.RegisterRequest;
import com.enigma.tokonyadia_api.dto.response.AuthResponse;
import com.enigma.tokonyadia_api.dto.response.RegisterResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Seller;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.service.*;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private CustomerService customerService;
    @Mock
    private SellerService sellerService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    AuthServiceImpl authService;


    @Test
    void shouldReturnAuthResponseWhileLogin() {
        AuthRequest request = new AuthRequest();
        request.setUsername("username");
        request.setPassword("password");

        // Mocking Authentication
        UserAccount userAccount = new UserAccount();
        userAccount.setId("user-1");
        userAccount.setRole(UserRole.ROLE_CUSTOMER); // Assuming Role is an entity class

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userAccount);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.doNothing().when(validationUtil).validate(request);
        Mockito.when(refreshTokenService.createToken(userAccount.getId())).thenReturn("refresh-token");
        Mockito.when(jwtService.generateAccessToken(userAccount)).thenReturn("access-token");

        AuthResponse actualResponse = authService.login(request);

        assertEquals("access-token", actualResponse.getAccessToken());
        assertEquals("refresh-token", actualResponse.getRefreshToken());
        assertEquals(userAccount.getRole().getDescription(), actualResponse.getRole());
    }

    @Test
    void shouldRegisterCustomerSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("customerUser");
        request.setPassword("password");
        request.setName("Customer Name");
        request.setRole("PELANGGAN");

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("customerUser");
        userAccount.setPassword("encodedPassword"); // Simulasi password terenkripsi
        userAccount.setRole(UserRole.ROLE_CUSTOMER);

        RegisterResponse expectedResponse = new RegisterResponse();
        expectedResponse.setUsername(userAccount.getUsername());

        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.doNothing().when(validationUtil).validate(request);
        Mockito.when(userService.create(Mockito.any(UserAccount.class))).thenReturn(userAccount);
        Mockito.when(customerService.create(Mockito.any(Customer.class))).thenReturn(Mockito.mock(Customer.class));

        RegisterResponse actualResponse = authService.register(request);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());

    }

    @Test
    void shouldRegisterSellerSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("sellerUser");
        request.setPassword("password");
        request.setName("Seller Name");
        request.setEmail("seller@email.com");
        request.setPhoneNumber("9999");
        request.setRole("PENJUAL");

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("sellerUser");
        userAccount.setPassword("encodedPassword"); // Simulasi password terenkripsi
        userAccount.setRole(UserRole.ROLE_SELLER);

        Seller seller = new Seller();
        seller.setName(request.getName());
        seller.setEmail(request.getEmail());
        seller.setPhoneNumber(request.getPhoneNumber());
        seller.setUserAccount(userAccount);

        RegisterResponse expectedResponse = new RegisterResponse();
        expectedResponse.setUsername(userAccount.getUsername());

        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.doNothing().when(validationUtil).validate(request);
        Mockito.when(userService.create(Mockito.any(UserAccount.class))).thenReturn(userAccount);
        Mockito.when(sellerService.create(Mockito.any(Seller.class))).thenReturn(Mockito.mock(Seller.class));

        RegisterResponse actualResponse = authService.register(request);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());

    }


    @Test
    void shouldThrowNotFoundWhenRoleIsAdmin() {
        RegisterRequest request = new RegisterRequest();
        request.setRole("ADMIN");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(request);
        });

        assertNotEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertNotEquals(Constant.ERROR_ROLE_NOT_FOUND, exception.getReason());
    }

    @Test
    void shouldThrowNotFoundWhenRoleIsInvalid() {
        RegisterRequest request = new RegisterRequest();
        request.setRole("INVALID_ROLE");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(request);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(Constant.ERROR_ROLE_NOT_FOUND, exception.getReason());
    }

    @Test
    void shouldThrowBadRequestWhenValidationFails() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("customerUser");
        request.setPassword("password");
        request.setRole("PELANGGAN");

        Mockito.doThrow(new RuntimeException("Validation failed")).when(validationUtil).validate(request);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation failed", exception.getReason());
    }

    @Test
    void shouldThrowBadRequestWhenExceptionOccurs() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("customerUser");
        request.setPassword("password");
        request.setRole("PELANGGAN");

        Mockito.doThrow(new RuntimeException("Validation error")).when(validationUtil).validate(request);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        // Arrange
        String token = "valid-refresh-token";
        String userId = "user123";

        UserAccount userAccount = new UserAccount();
        userAccount.setRole(UserRole.ROLE_CUSTOMER);

        String newRefreshToken = "newRefreshToken";
        String newAccessToken = "newAccessToken";

        // Mock behavior
        Mockito.when(refreshTokenService.getUserIdByToken(token)).thenReturn(userId);
        Mockito.when(userService.getById(userId)).thenReturn(userAccount);
        Mockito.when(refreshTokenService.rotateRefreshToken(userId)).thenReturn(newRefreshToken);
        Mockito.when(jwtService.generateAccessToken(userAccount)).thenReturn(newAccessToken);

        // Act
        AuthResponse actualResponse = authService.refreshToken(token);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(newAccessToken, actualResponse.getAccessToken());
        assertEquals(newRefreshToken, actualResponse.getRefreshToken());
        assertEquals(userAccount.getRole().getDescription(), actualResponse.getRole());
    }

    @Test
    void shouldLogoutSuccessfully() {
        // Arrange
        String accessToken = "valid-access-token";
        String userId = "user123";

        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);

        // Setup Authentication Mock
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userAccount);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mocking dependencies
        Mockito.doNothing().when(refreshTokenService).deleteRefreshToken(userId);
        Mockito.doNothing().when(jwtService).blacklistAccessToken(accessToken);

        // Act
        authService.logout(accessToken);

        // Assert
        Mockito.verify(refreshTokenService, Mockito.times(1)).deleteRefreshToken(userId);
        Mockito.verify(jwtService, Mockito.times(1)).blacklistAccessToken(accessToken);
    }

}