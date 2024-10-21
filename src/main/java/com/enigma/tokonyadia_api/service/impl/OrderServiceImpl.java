package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.OrderStatus;
import com.enigma.tokonyadia_api.dto.request.DraftOrderRequest;
import com.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.UpdateOrderStatusRequest;
import com.enigma.tokonyadia_api.dto.response.OrderDetailResponse;
import com.enigma.tokonyadia_api.dto.response.OrderResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Order;
import com.enigma.tokonyadia_api.entity.OrderDetail;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.repository.OrderRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.service.OrderService;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.specification.OrderSpecification;
import com.enigma.tokonyadia_api.util.MapperUtil;
import com.enigma.tokonyadia_api.util.SortUtil;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ValidationUtil validationUtil;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse createDraft(DraftOrderRequest request) {
        validationUtil.validate(request);
        Customer customer = customerService.getOne(request.getCustomerId());
        Order order = Order.builder()
                .customer(customer)
                .orderStatus(OrderStatus.DRAFT)
                .orderDetails(new ArrayList<>())
                .build();
        Order savedOrder = orderRepository.saveAndFlush(order);
        return MapperUtil.toOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponse> getAllOrders(PagingAndSortingRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Order> transactionSpecification = OrderSpecification.getSpecification(request);
        Page<Order> transactionPage = orderRepository.findAll(transactionSpecification, pageable);
        return transactionPage.map(MapperUtil::toOrderResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDetailResponse> getOrderDetail(String orderId) {
        Order order = getOne(orderId);
        return order.getOrderDetails().stream()
                .map(MapperUtil::toOrderDetailResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse addOrderDetails(String orderId, List<OrderDetailRequest> orderDetailRequests) {
        validationUtil.validate(orderDetailRequests);
        Order order = getOne(orderId);

        if (order.getOrderStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_NON_DRAFT);
        }

        for (OrderDetailRequest request : orderDetailRequests) {

            Product product = productService.getOne(request.getProductId());
            if (product.getStock() < request.getQty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_INSUFFICIENT_STOCK);

            boolean isExist = false;
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail.getProduct().getId().equalsIgnoreCase(product.getId())) {
                    if (orderDetail.getQty() + request.getQty() > product.getStock()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_INSUFFICIENT_STOCK);
                    }
                    orderDetail.setQty(orderDetail.getQty() + request.getQty());
                    orderDetail.setPrice(product.getPrice());
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                OrderDetail orderDetail = OrderDetail.builder()
                        .product(product)
                        .order(order)
                        .qty(request.getQty())
                        .price(product.getPrice())
                        .build();
                order.getOrderDetails().add(orderDetail);
            }
        }

        Order updatedOrder = orderRepository.save(order);
        return MapperUtil.toOrderResponse(updatedOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse updateOrderDetails(String orderId, String detailsId, OrderDetailRequest request) {
        Order order = getOne(orderId);
        // TODO : Update transactionDetail
        if (order.getOrderStatus() != OrderStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_UPDATE_ITEMS_NON_DRAFT);

        OrderDetail orderDetail = order.getOrderDetails().stream()
                .filter(details -> details.getId().equals(detailsId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ORDER_DETAIL_NOT_FOUND));

        Product product = productService.getOne(request.getProductId());
        orderDetail.setProduct(product);
        orderDetail.setQty(request.getQty());
        orderDetail.setPrice(product.getPrice());
        Order updatedOrder = orderRepository.save(order);
        return MapperUtil.toOrderResponse(updatedOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        validationUtil.validate(request);
        Order order = getOne(orderId);
        if (request.getStatus().equals(OrderStatus.CONFIRMED)){
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                Product product = orderDetail.getProduct();
                Integer quantity = orderDetail.getQty();
                if (product.getStock() < quantity)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_INSUFFICIENT_STOCK);
                product.setStock(product.getStock() - quantity);
                productService.updateProductAndImage(product);
            }
        }
        order.setOrderStatus(request.getStatus());
        Order updatedOrder = orderRepository.save(order);
        return MapperUtil.toOrderResponse(updatedOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse checkoutOrder(String orderId) {
        Order order = getOne(orderId);
        if (order.getOrderStatus() != OrderStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_CHECKOUT_ITEM_FROM_NON_DRAFT);

        order.setOrderStatus(OrderStatus.PENDING);
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Product product = orderDetail.getProduct();
            product.setStock(product.getStock() - orderDetail.getQty());
            productService.updateProductAndImage(product);
        }

        Order updatedOrder = orderRepository.save(order);
        return MapperUtil.toOrderResponse(updatedOrder);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse deleteOrderDetails(String orderId, String detailsId) {
        Order order = getOne(orderId);
        if (order.getOrderStatus() != OrderStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_REMOVE_ITEMS_FROM_NON_DRAFT);

        order.getOrderDetails().stream().findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction detail is empty")
        );
        order.getOrderDetails().removeIf(details -> details.getId().equals(detailsId));
        Order updatedOrder = orderRepository.save(order);
        return MapperUtil.toOrderResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getOrderById(String orderId) {
        Order order = getOne(orderId);
        return MapperUtil.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Order getOne(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ORDER_NOT_FOUND));
        order.getOrderDetails().size();
        return order;
    }
}