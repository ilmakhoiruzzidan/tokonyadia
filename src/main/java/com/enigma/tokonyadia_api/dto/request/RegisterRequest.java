package com.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
}
