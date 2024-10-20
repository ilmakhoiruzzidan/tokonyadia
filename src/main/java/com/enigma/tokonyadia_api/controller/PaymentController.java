package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.MidtransNotificationRequest;
import com.enigma.tokonyadia_api.dto.request.PaymentRequest;
import com.enigma.tokonyadia_api.dto.response.PaymentResponse;
import com.enigma.tokonyadia_api.service.PaymentService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(Constant.PAYMENT_API)
@RequiredArgsConstructor
public class PaymentController {
    public final PaymentService paymentService;


    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse paymentResponse = paymentService.createPayment(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CHECKOUT_ORDER, paymentResponse);
    }

    @PostMapping(path = "/notifications")
    public ResponseEntity<?> handleNotification(@RequestBody Map<String, String> request) {
        MidtransNotificationRequest midtransNotificationRequest = MidtransNotificationRequest.builder()
                .transactionTime(request.get("transaction_time"))
                .orderId(request.get("order_id"))
                .grossAmount(request.get("gross_amount"))
                .statusCode(request.get("status_code"))
                .transactionStatus(request.get("transaction_status"))
                .signatureKey(request.get("signature_key"))
                .build();
        paymentService.getNotification(midtransNotificationRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, "OK", null);
    }

}
