package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Order;
import com.enigma.tokonyadia_api.entity.OrderDetail;
import com.enigma.tokonyadia_api.repository.OrderDetailRepository;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    @Override
    public List<OrderDetail> addOrderDetail(Order order, List<OrderDetailRequest> orderDetailRequests) {

        ArrayList<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailRequest transactionDetail : orderDetailRequests) {
            Product product = productService.getOne(transactionDetail.getProductId());

            OrderDetail td = OrderDetail.builder()
                    .product(product)
                    .qty(transactionDetail.getQty())
                    .order(order)
                    .price(product.getPrice())
                    .build();


            orderDetailRepository.save(td);

            orderDetails.add(td);
        }

        return orderDetails;
    }
}
