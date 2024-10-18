package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.request.PaymentRequest;
import com.enigma.tokonyadia_api.dto.response.PaymentResponse;
import com.enigma.tokonyadia_api.repository.PaymentRepository;
import com.enigma.tokonyadia_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

    }
}
