package com.enigma.tokonyadia_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String orderId;
    private LocalDateTime transactionDate;
    private SimpleCustomerResponse customer;
    private List<SimpleOrderDetailResponse> orderDetail;
    private String transactionStatus;

    @JsonProperty("transactionDate")
    public String getFormattedTransactionDate() {
        return this.transactionDate != null ? this.transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}
