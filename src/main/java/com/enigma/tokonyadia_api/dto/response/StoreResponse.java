package com.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {
    private String id;
    private String name;
    private String address;
    private String noSiup;
    private String phoneNumber;
}
