package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.DraftOrderRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import com.enigma.tokonyadia_api.dto.request.UpdateOrderStatusRequest;
import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.OrderDetailResponse;
import com.enigma.tokonyadia_api.dto.response.OrderResponse;
import com.enigma.tokonyadia_api.service.OrderService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Order", description = "APIs for managing orders, including creating, updating, retrieving, and deleting order details")
public class OrderController {
    private static class CommonResponseOrderResponse extends CommonResponse<OrderResponse> {}
    private static class CommonResponseOrderDetailResponse extends CommonResponse<OrderDetailResponse> {}

    private final OrderService orderService;

    @Operation(summary = "Create draft order",
            description = "This endpoint allows creating a draft order. The order is created in the DRAFT status, awaiting further updates.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Draft order created successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PostMapping("/draft")
    public ResponseEntity<?> createDraft(@RequestBody DraftOrderRequest request) {
        OrderResponse orderResponse = orderService.createDraft(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "OK", orderResponse);
    }

    @Operation(summary = "Get order details",
            description = "Retrieve the details of a specific order by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order details retrieved successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderDetailResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @GetMapping("/{orderId}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId) {
        List<OrderDetailResponse> orderDetailResponse = orderService.getOrderDetail(orderId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER_DETAIL, orderDetailResponse);
    }

    @Operation(summary = "Add order detail",
            description = "Add a new detail to a specific order by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order detail added successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PostMapping("/{orderId}/details")
    public ResponseEntity<?> addOrderToDetails(@PathVariable String orderId, @Valid @RequestBody List<OrderDetailRequest> request) {
        OrderResponse orderResponse = orderService.addOrderDetails(orderId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_ADD_ITEMS_TO_ORDER, orderResponse);
    }

    @Operation(summary = "Update order detail",
            description = "Update an existing order detail by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order detail updated successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order or detail not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PutMapping("/{orderId}/details/{detailsId}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable String orderId, @PathVariable String detailsId, @Valid @RequestBody OrderDetailRequest orderDetailRequest) {
        OrderResponse orderResponse = orderService.updateOrderDetails(orderId, detailsId, orderDetailRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_ORDER, orderResponse);
    }

    @Operation(summary = "Remove order detail",
            description = "Remove an order detail by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order detail removed successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order or detail not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @DeleteMapping("/{orderId}/details/{detailsId}")
    public ResponseEntity<?> deleteOrderById(@PathVariable String orderId, @PathVariable String detailsId) {
        orderService.deleteOrderDetails(orderId, detailsId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_REMOVE_ORDER_DETAIL, null);
    }

    @Operation(summary = "Update order status",
            description = "Update the status of a specific order by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status updated successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid status data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_ORDER_STATUS, orderResponse);
    }

    @Operation(summary = "Get all orders",
            description = "Retrieve a paginated list of all orders with optional filtering by date and query.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid query parameters", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
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

    @Operation(summary = "Get order by ID",
            description = "Retrieve a specific order by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order retrieved successfully", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER_BY_ID, orderResponse);
    }
}
