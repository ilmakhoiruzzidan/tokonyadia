package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.util.MapperUtil;
import com.enigma.tokonyadia_api.dto.request.UserRequest;
import com.enigma.tokonyadia_api.dto.request.UserUpdatePasswordRequest;
import com.enigma.tokonyadia_api.dto.response.UserResponse;

import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.repository.UserAccountRepository;
import com.enigma.tokonyadia_api.service.UserService;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${tokonyadia.user-admin}")
    private String USERNAME_ADMIN;
    @Value("${tokonyadia.user-password}")
    private String PASSWORD_ADMIN;

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;


    @PostConstruct
    public void initUser() {
        boolean isExist = userAccountRepository.existsByUsername(USERNAME_ADMIN);
        if (isExist) return;
        UserAccount userAccount = UserAccount.builder()
                .username(USERNAME_ADMIN)
                .password(PASSWORD_ADMIN)
                .role(UserRole.ROLE_ADMIN)
                .build();
        userAccountRepository.saveAndFlush(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse create(UserRequest request) {
        try {
            UserRole userRole = UserRole.findByDescription(request.getRole());
            if (userRole == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ROLE_NOT_FOUND);
            // constructor userAccount
            UserAccount userAccount = UserAccount.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(userRole)
                    .build();
            // save userAccount
            userAccountRepository.save(userAccount);
            return MapperUtil.toUserResponse(userAccount);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_USERNAME_DUPLICATE);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount create(UserAccount userAccount) {
        return userAccountRepository.saveAndFlush(userAccount);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount getById(String id) {
        return userAccountRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_USER_NOT_FOUND)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return MapperUtil.toUserResponse(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(String id, UserUpdatePasswordRequest request) {
        validationUtil.validate(request);
        UserAccount userAccount = getById(id);

        if (!passwordEncoder.matches(request.getCurrentPassword(), userAccount.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_INVALID_CREDENTIAL);
        }

        userAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccountRepository.saveAndFlush(userAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(Constant.ERROR_USERNAME_NOT_FOUND)
        );
    }
}
