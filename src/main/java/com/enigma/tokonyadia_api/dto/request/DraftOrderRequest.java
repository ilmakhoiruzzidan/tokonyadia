package com.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftOrderRequest {
    @NotBlank
    private String customerId;
}
