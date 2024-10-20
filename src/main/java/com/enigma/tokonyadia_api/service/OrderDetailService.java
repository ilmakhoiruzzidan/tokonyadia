package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import com.enigma.tokonyadia_api.entity.Order;
import com.enigma.tokonyadia_api.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> addOrderDetail(Order order, List<OrderDetailRequest> orderDetailRequests);
}
