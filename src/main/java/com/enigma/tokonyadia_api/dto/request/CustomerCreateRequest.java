package com.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCreateRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
}
