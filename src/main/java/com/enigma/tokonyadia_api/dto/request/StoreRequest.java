package com.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequest {
    private String name;
    private String address;
    private String noSiup;
    private String phoneNumber;
}
