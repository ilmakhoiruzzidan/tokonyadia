package com.enigma.tokonyadia_api.audit;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.entity.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditorAwareImplTest {

    private AuditorAwareImpl auditorAware;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        auditorAware = new AuditorAwareImpl();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldReturnUsernameWhenAuthenticated() {
        // Mock authenticated user
        UserAccount mockUserAccount = mock(UserAccount.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockUserAccount);
        when(mockUserAccount.getUsername()).thenReturn("john_doe");

        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Call getCurrentAuditor and assert it returns the username in uppercase
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();
        assertEquals(Optional.of("JOHN_DOE"), currentAuditor);
    }

    @Test
    void shouldReturnSystemWhenAnonymousUser() {
        // Mock an anonymous user
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Call getCurrentAuditor and assert it returns the system user
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();
        assertEquals(Optional.of(Constant.SYSTEM), currentAuditor);
    }

    @Test
    void shouldReturnSystemWhenAuthenticationIsNull() {
        // Simulate the case where authentication is null
        when(securityContext.getAuthentication()).thenReturn(null);

        // Call getCurrentAuditor and assert it returns the system user
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();
        assertEquals(Optional.of(Constant.SYSTEM), currentAuditor);
    }

    @Test
    void shouldReturnTrueWHenAuthenticationEqualsAnonymousUser() {
        when(authentication.getPrincipal()).thenReturn("anonymousUser");


    }
}
