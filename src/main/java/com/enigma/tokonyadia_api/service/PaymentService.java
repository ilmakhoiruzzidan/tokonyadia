package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.PaymentRequest;
import com.enigma.tokonyadia_api.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
}
