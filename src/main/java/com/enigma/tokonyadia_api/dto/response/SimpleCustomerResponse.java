package com.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleCustomerResponse {
    private String id;
    private String name;
}
