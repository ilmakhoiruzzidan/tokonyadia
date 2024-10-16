package com.enigma.tokonyadia_api.security;

import com.enigma.tokonyadia_api.entity.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.valueOf(authentication));
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            // Return a default value or handle cases where no user is authenticated
            return Optional.of("SYSTEM");
        }

        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return Optional.of(userAccount.getUsername().toUpperCase());
    }
}
