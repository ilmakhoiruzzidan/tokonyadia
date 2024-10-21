package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.DraftOrderRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import com.enigma.tokonyadia_api.dto.request.UpdateOrderStatusRequest;
import com.enigma.tokonyadia_api.dto.response.OrderDetailResponse;
import com.enigma.tokonyadia_api.dto.response.OrderResponse;
import com.enigma.tokonyadia_api.service.OrderService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Constant.ORDER_API)
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/draft")
    public ResponseEntity<?> createDraft(@RequestBody DraftOrderRequest request) {
        OrderResponse orderResponse = orderService.createDraft(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "OK", orderResponse);
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId) {
        List<OrderDetailResponse> orderDetailResponse = orderService.getOrderDetail(orderId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER_DETAIL, orderDetailResponse);
    }

    @PostMapping("/{orderId}/details")
    public ResponseEntity<?> addOrderToDetails(@PathVariable String orderId, @Valid @RequestBody List<OrderDetailRequest> request) {
        OrderResponse orderResponse = orderService.addOrderDetails(orderId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_ADD_ITEMS_TO_ORDER, orderResponse);
    }

    @PutMapping("/{orderId}/details/{detailsId}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable String orderId, @PathVariable String detailsId, @Valid @RequestBody OrderDetailRequest orderDetailRequest) {
        OrderResponse orderResponse = orderService.updateOrderDetails(orderId, detailsId, orderDetailRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_ORDER, orderResponse);
    }

    @DeleteMapping("/{orderId}/details/{detailsId}")
    public ResponseEntity<?> deleteOrderById(@PathVariable String orderId, @PathVariable String detailsId) {
        orderService.deleteOrderDetails(orderId, detailsId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_REMOVE_ORDER_DETAIL, null);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_ORDER_STATUS, orderResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        PagingAndSortingRequest pagingAndSortingRequest = PagingAndSortingRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();
        Page<OrderResponse> transactionPage = orderService.getAllOrders(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, Constant.SUCCESS_GET_ALL_ORDER, transactionPage);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER_BY_ID, orderResponse);
    }
}
