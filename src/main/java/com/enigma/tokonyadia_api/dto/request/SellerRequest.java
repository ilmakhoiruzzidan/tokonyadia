package com.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerRequest {
    private String name;
    private String email;
    private String phoneNumber;
}
