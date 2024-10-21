package com.enigma.tokonyadia_api.util;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.response.*;
import com.enigma.tokonyadia_api.entity.*;

import java.util.Collections;
import java.util.List;

public class MapperUtil {

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .images(getProductImages(product))
                .store(toStoreResponse(product.getStore()))
                .category(toCategoryResponse(product.getCategory()))
                .description(product.getDescription())
                .stock(product.getStock())
                .build();
    }

    public static StoreResponse toStoreResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .noSiup(store.getNoSiup())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress())
                .build();
    }

    public static OrderResponse toOrderResponse(Order order) {

        SimpleCustomerResponse customerResponse = SimpleCustomerResponse.builder()
                .id(order.getCustomer().getId())
                .name(order.getCustomer().getName())
                .build();

        List<SimpleOrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                .map(orderDetail -> SimpleOrderDetailResponse.builder()
                        .id(orderDetail.getId())
                        .product(SimpleProductResponse.builder()
                                .productName(orderDetail.getProduct().getName())
                                .stock(orderDetail.getProduct().getStock())
                                .price(orderDetail.getProduct().getPrice())
                                .images(getProductImages(orderDetail.getProduct()))
                                .categoryName(orderDetail.getProduct().getCategory().getName())
                                .storeName(orderDetail.getProduct().getStore().getName())
                                .build())
                        .qty(orderDetail.getQty())
                        .price(orderDetail.getPrice())
                        .build())
                .toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .transactionDate(order.getTransDate())
                .customer(customerResponse)
                .transactionStatus(order.getOrderStatus().name())
                .orderDetail(orderDetailResponses)
                .build();
    }

    public static OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        Order order = Order.builder()
                .id(orderDetail.getOrder().getId())
                .transDate(orderDetail.getOrder().getTransDate())
                .customer(orderDetail.getOrder().getCustomer())
                .build();

        Product product = Product.builder()
                .id(orderDetail.getProduct().getId())
                .name(orderDetail.getProduct().getName())
                .description(orderDetail.getProduct().getDescription())
                .store(orderDetail.getProduct().getStore())
                .stock(orderDetail.getProduct().getStock())
                .build();

        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .qty(orderDetail.getQty())
                .order(MapperUtil.toOrderResponse(order))
                .product(MapperUtil.toProductResponse(product))
                .build();
    }

    public static UserResponse toUserResponse(UserAccount userAccount) {
        return UserResponse.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .role(userAccount.getRole().getDescription())
                .build();
    }

    public static RegisterResponse toRegisterResponse(UserAccount userAccount) {
        return RegisterResponse.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .role(userAccount.getRole().getDescription())
                .build();
    }

    public static ProductCategoryResponse toCategoryResponse(Category category) {
        return ProductCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .build();
    }

    public static SellerResponse toSellerResponse(Seller seller) {
        return SellerResponse.builder()
                .id(seller.getId())
                .name(seller.getName())
                .phoneNumber(seller.getPhoneNumber())
                .email(seller.getEmail())
                .build();
    }

    public static PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .tokenSnap(payment.getTokenSnap())
                .redirectUrl(payment.getRedirectUrl())
                .build();
    }

    public static List<FileResponse> getProductImages(Product product) {
        return product.getImages() != null && !product.getImages().isEmpty() ?
                product.getImages().stream().map(file -> FileResponse.builder()
                        .id(file.getId())
                        .url(Constant.IMAGE_API + "/" + file.getId())
                        .build()).toList() :
                Collections.emptyList();
    }
}
