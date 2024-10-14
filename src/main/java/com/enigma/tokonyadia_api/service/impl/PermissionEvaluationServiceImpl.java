package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.service.PermissionEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionEvaluationServiceImpl implements PermissionEvaluationService {
    private final CustomerService customerService;

    @Override
    public boolean hasAccessToCustomer(String customerId, String id) {
        Customer customer = customerService.getOne(customerId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return !userAccount.getRole().equals(UserRole.ROLE_CUSTOMER) || userAccount.getId().equals(customer.getUserAccount().getId());
    }
}
