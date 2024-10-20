package com.enigma.tokonyadia_api.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private String id;
    private Long price;
    private Integer qty;
    private ProductResponse product;
    private OrderResponse order;
}
