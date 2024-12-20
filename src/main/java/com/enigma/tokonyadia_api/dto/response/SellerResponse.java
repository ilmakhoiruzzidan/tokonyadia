package com.enigma.tokonyadia_api.dto.response;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
}
