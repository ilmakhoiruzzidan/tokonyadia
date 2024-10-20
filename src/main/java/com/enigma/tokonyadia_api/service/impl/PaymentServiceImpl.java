package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.client.MidtransClient;
import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.OrderStatus;
import com.enigma.tokonyadia_api.constant.PaymentStatus;
import com.enigma.tokonyadia_api.dto.request.*;
import com.enigma.tokonyadia_api.dto.response.MidtransSnapResponse;
import com.enigma.tokonyadia_api.dto.response.PaymentResponse;
import com.enigma.tokonyadia_api.entity.Order;
import com.enigma.tokonyadia_api.entity.OrderDetail;
import com.enigma.tokonyadia_api.entity.Payment;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.repository.PaymentRepository;
import com.enigma.tokonyadia_api.service.OrderService;
import com.enigma.tokonyadia_api.service.PaymentService;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.util.HashUtil;
import com.enigma.tokonyadia_api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final MidtransClient midtransClient;

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderService.getOne(request.getOrderId());

        if (!order.getOrderStatus().equals(OrderStatus.DRAFT))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_CHECKOUT_ITEM_FROM_NON_DRAFT);

        long amount = 0L;
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Integer quantity = orderDetail.getQty();
            Long price = orderDetail.getPrice();
            amount += quantity * price;

            Product product = orderDetail.getProduct();
            product.setStock(product.getStock() - quantity);
            productService.updateProduct(product);
        }

        MidtransTransactionRequest midtransTransactionRequest = MidtransTransactionRequest.builder()
                .orderId(order.getId())
                .grossAmount(amount)
                .build();

        MidtransPaymentRequest midtransPaymentRequest = MidtransPaymentRequest.builder()
                .transactionDetail(midtransTransactionRequest)
                .enabledPayments(List.of("bca_va", "gopay", "shopeepay", "other_qris"))
                .build();

        String headerValue = "Basic " + Base64.getEncoder().encodeToString(MIDTRANS_SERVER_KEY.getBytes(StandardCharsets.UTF_8));
        MidtransSnapResponse snapTransaction = midtransClient.createSnapTransaction(midtransPaymentRequest, headerValue);

        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .paymentStatus(PaymentStatus.PENDING)
                .createdDate(LocalDateTime.now())
                .tokenSnap(snapTransaction.getToken())
                .redirectUrl(snapTransaction.getRedirectUrl())
                .build();
        paymentRepository.saveAndFlush(payment);

        UpdateOrderStatusRequest updateOrderStatusRequest = UpdateOrderStatusRequest.builder()
                .status(OrderStatus.PENDING)
                .build();

        orderService.updateOrderStatus(order.getId(), updateOrderStatusRequest);
        return MapperUtil.toPaymentResponse(payment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void getNotification(MidtransNotificationRequest request) {
        log.info("Start getNotification: {}", System.currentTimeMillis());
        if (!validateSignatureKey(request)) {
            log.error("Invalid signature key for order: {}", request.getOrderId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid signature key");
        }

        Payment payment = paymentRepository.findByOrder_Id(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_PAYMENT_NOT_FOUND));

        PaymentStatus newPaymentStatus = PaymentStatus.findByDesc(request.getTransactionStatus());
        log.info("Payment status for order {}: {}", request.getOrderId(), newPaymentStatus);
        payment.setPaymentStatus(newPaymentStatus);
        payment.setLastModifiedDate(LocalDateTime.now());

        Order order = orderService.getOne(request.getOrderId());

        if (newPaymentStatus != null && newPaymentStatus.equals(PaymentStatus.SETTLEMENT)) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }
        log.info("Payment status after update for order {}: {}", request.getOrderId(), newPaymentStatus);

        UpdateOrderStatusRequest updateOrderStatusRequest = UpdateOrderStatusRequest.builder()
                .status(order.getOrderStatus())
                .build();
        orderService.updateOrderStatus(order.getId(), updateOrderStatusRequest);
        paymentRepository.saveAndFlush(payment);
        log.info("End getNotification: {}", System.currentTimeMillis());
    }

    private boolean validateSignatureKey(MidtransNotificationRequest request) {
        String rawString = request.getOrderId() + request.getStatusCode() + request.getGrossAmount() + MIDTRANS_SERVER_KEY;
        log.info("Order ID: {}", request.getOrderId());
        log.info("Status Code: {}", request.getStatusCode());
        log.info("Gross Amount: {}", request.getGrossAmount());
        log.info("Raw String for signature: {}", rawString);
        String signatureKey = HashUtil.encryptThisString(rawString);
        return request.getSignatureKey().equalsIgnoreCase(signatureKey);
    }

}
