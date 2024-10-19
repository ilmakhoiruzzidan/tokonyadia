package com.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleProductResponse {
    private String productName;
    private Integer stock;
    private Long price;
    private String categoryName;
    private String storeName;
}
