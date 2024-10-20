package com.enigma.tokonyadia_api.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private String customerId;
    private List<OrderDetailRequest> orderDetail;
}
