package com.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreProductResponse {
    private String storeId;
    private String storeName;
    private List<SimpleProductResponse> products;
}
