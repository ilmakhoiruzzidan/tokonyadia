package com.enigma.tokonyadia_api.audit;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.entity.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.valueOf(authentication));
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            // Return a default value or handle cases where no user is authenticated
            return Optional.of(Constant.SYSTEM);
        }

        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return Optional.of(userAccount.getUsername().toUpperCase());
    }
}
