package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.OrderStatus;
import com.enigma.tokonyadia_api.dto.request.DraftOrderRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.OrderResponse;
import com.enigma.tokonyadia_api.dto.response.SimpleCustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Order;
import com.enigma.tokonyadia_api.repository.OrderRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.util.SortUtil;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCreateDraftSuccessfully() {
        // Arrange
        DraftOrderRequest request = new DraftOrderRequest();
        request.setCustomerId("customer123");

        Customer customer = new Customer();
        customer.setId("customer123");

        Order savedOrder = new Order();
        savedOrder.setId("order123");
        savedOrder.setCustomer(customer);
        savedOrder.setOrderStatus(OrderStatus.DRAFT);
        savedOrder.setOrderDetails(new ArrayList<>());

        // Mocking the behavior of validation and customer retrieval
        Mockito.doNothing().when(validationUtil).validate(request);
        when(customerService.getOne(request.getCustomerId())).thenReturn(customer);
        when(orderRepository.saveAndFlush(Mockito.any(Order.class))).thenReturn(savedOrder);

        // Act
        OrderResponse actualResponse = orderService.createDraft(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(savedOrder.getId(), actualResponse.getOrderId());

        // Verify interactions
        Mockito.verify(validationUtil, Mockito.times(1)).validate(request);
        Mockito.verify(customerService, Mockito.times(1)).getOne(request.getCustomerId());
        Mockito.verify(orderRepository, Mockito.times(1)).saveAndFlush(Mockito.any(Order.class));
    }

    @Test
    void shouldGetAllOrdersSuccessfully() {
        // Arrange
        PagingAndSortingRequest request = new PagingAndSortingRequest();
        request.setSortBy("id"); // Contoh sortBy
        request.setPage(0); // Halaman pertama
        request.setSize(10); // Ukuran halaman
        request.setSortBy("name, asc");

        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);



        // Buat contoh Order
        Order order = new Order();
        order.setId("order123");
        order.setCustomer(Mockito.mock(Customer.class));
        order.setOrderStatus(OrderStatus.DRAFT);
        order.setOrderDetails(new ArrayList<>());

        // Buat Page untuk Order
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order), pageable,1);

        // Mocking perilaku orderRepository
        Mockito.when(orderRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(orderPage);

        OrderResponse expectedResponse = new OrderResponse();
        expectedResponse.setOrderId(order.getId());
        expectedResponse.setCustomer(Mockito.mock(SimpleCustomerResponse.class));

        // Act
        Page<OrderResponse> actualResponse = orderService.getAllOrders(request);

        // Assert
        assertEquals(1, actualResponse.getTotalElements());
        assertEquals(expectedResponse.getOrderId(), actualResponse.getContent().get(0).getOrderId());
        Mockito.verify(orderRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));

    }
}