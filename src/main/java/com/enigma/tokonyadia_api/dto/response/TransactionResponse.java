package com.enigma.tokonyadia_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String transactionId;
    private LocalDateTime transactionDate;
    private CustomerResponse customer;
    private List<SimpleTransactionDetailResponse> transactionDetail;
}
