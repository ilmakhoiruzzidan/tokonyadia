package com.enigma.tokonyadia_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransExpiryRequest {
    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("duration")
    private Integer duration;
}
