package com.enigma.tokonyadia_api.client;

import com.enigma.tokonyadia_api.config.FeignClientConfiguration;
import com.enigma.tokonyadia_api.dto.request.MidtransPaymentRequest;
import com.enigma.tokonyadia_api.dto.response.MidtransSnapResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "midtransApp", url = "${midtrans.app.url}", configuration = FeignClientConfiguration.class)
public interface MidtransAppClient {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/snap/v1/transactions"
    )
    MidtransSnapResponse createSnapTransaction(
            @RequestBody MidtransPaymentRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String headerAuthorization
    );
}
