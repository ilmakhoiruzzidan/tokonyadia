package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.MidtransNotificationRequest;
import com.enigma.tokonyadia_api.dto.request.PaymentRequest;
import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.PaymentResponse;
import com.enigma.tokonyadia_api.service.PaymentService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Constant.PAYMENT_API)
@RequiredArgsConstructor
@Tag(name = "Payment", description = "APIs for managing payments, including creating payments, handling notifications, and checking payment status by order ID")
public class PaymentController {
    private static class CommonResponsePaymentResponse extends CommonResponse<PaymentResponse> {
    }

    public final PaymentService paymentService;

    @Operation(summary = "Create a payment",
            description = "This endpoint initiates a payment request for a specified order. The payment amount and order details are provided in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment created successfully", content = @Content(schema = @Schema(implementation = CommonResponsePaymentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse paymentResponse = paymentService.createPayment(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CHECKOUT_ORDER, paymentResponse);
    }

    @Operation(summary = "Handle payment notifications",
            description = "Handles asynchronous payment notifications (webhooks) from Midtrans, updating the payment status based on the notification content.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notification processed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid notification data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PostMapping(path = "/notifications")
    public ResponseEntity<?> handleNotification(@RequestBody MidtransNotificationRequest request) {
        paymentService.getNotification(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "OK", null);
    }

    @Operation(summary = "Get payment status by order ID",
            description = "Retrieves the payment status for a specific order by its order ID. This checks the current status of the payment in the Midtrans system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment status retrieved successfully", content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order or payment not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping(path = "/{orderId}/status")
    public ResponseEntity<?> getStatusPaymentByOrderId(@PathVariable String orderId) {
        PaymentResponse paymentResponse = paymentService.getPaymentStatusByOrderId(orderId);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully get payment status", paymentResponse);
    }

}
