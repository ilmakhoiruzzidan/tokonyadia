package com.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftTransactionRequest {
    private String customerId;
}
