package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.UserRequest;
import com.enigma.tokonyadia_api.dto.request.UserUpdatePasswordRequest;
import com.enigma.tokonyadia_api.dto.response.UserResponse;
import com.enigma.tokonyadia_api.service.UserService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.USER_API)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        UserResponse userResponse = userService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_REGISTER_USER, userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo() {
        UserResponse userResponse = userService.getAuthentication();
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_CURRENT_USER_INFO, userResponse);
    }

    @PatchMapping("/{id}/update-password")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody UserUpdatePasswordRequest request) {
        userService.updatePassword(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PASSWORD, null);
    }
}
