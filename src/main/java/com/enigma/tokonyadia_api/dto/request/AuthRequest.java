package com.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
