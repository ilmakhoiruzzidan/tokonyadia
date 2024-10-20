package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.DraftOrderRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import com.enigma.tokonyadia_api.dto.request.UpdateOrderStatusRequest;
import com.enigma.tokonyadia_api.dto.response.OrderDetailResponse;
import com.enigma.tokonyadia_api.dto.response.OrderResponse;
import com.enigma.tokonyadia_api.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponse createDraft(DraftOrderRequest request);
    List<OrderDetailResponse> getOrderDetail(String orderId);
    OrderResponse getOrderById(String orderId);
    Order getOne(String orderId);
    Page<OrderResponse> getAllOrders(PagingAndSortingRequest request);
    OrderResponse addOrderDetails(String orderId, List<OrderDetailRequest> request);
    OrderResponse updateOrderDetails(String orderId, String detailsId, OrderDetailRequest request);
    OrderResponse checkoutOrder(String orderId);
    OrderResponse updateOrderStatus(String orderId, UpdateOrderStatusRequest request);
    OrderResponse deleteOrderDetails(String orderId, String detailsId);
}
