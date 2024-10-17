package com.enigma.tokonyadia_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String transactionStatus;

    @JsonProperty("transactionDate")
    public String getFormattedTransactionDate() {
        return this.transactionDate != null ? this.transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}
