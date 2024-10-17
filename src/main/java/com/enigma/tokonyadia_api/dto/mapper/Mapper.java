package com.enigma.tokonyadia_api.dto.mapper;

import com.enigma.tokonyadia_api.dto.response.*;
import com.enigma.tokonyadia_api.entity.*;

import java.util.List;

public class Mapper {

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
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

    public static TransactionResponse toTransactionResponse(Transaction transaction) {

        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(transaction.getCustomer().getId())
                .name(transaction.getCustomer().getName())
                .email(transaction.getCustomer().getEmail())
                .phoneNumber(transaction.getCustomer().getPhoneNumber())
                .build();

        List<SimpleTransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream()
                .map(transactionDetail -> SimpleTransactionDetailResponse.builder()
                        .id(transactionDetail.getId())
                        .product(Mapper.toProductResponse(transactionDetail.getProduct()))
                        .qty(transactionDetail.getQty())
                        .price(transactionDetail.getPrice())
                        .build())
                .toList();

        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .transactionDate(transaction.getTransDate())
                .customer(customerResponse)
                .transactionStatus(transaction.getStatus().name())
                .transactionDetail(transactionDetailResponses)
                .build();
    }

    public static TransactionDetailResponse toTransactionDetailResponse(TransactionDetail transactionDetail) {
        Transaction transaction = Transaction.builder()
                .id(transactionDetail.getTransaction().getId())
                .transDate(transactionDetail.getTransaction().getTransDate())
                .customer(transactionDetail.getTransaction().getCustomer())
                .build();

        Product product = Product.builder()
                .id(transactionDetail.getProduct().getId())
                .name(transactionDetail.getProduct().getName())
                .description(transactionDetail.getProduct().getDescription())
                .store(transactionDetail.getProduct().getStore())
                .stock(transactionDetail.getProduct().getStock())
                .build();

        return TransactionDetailResponse.builder()
                .id(transactionDetail.getId())
                .qty(transactionDetail.getQty())
                .transaction(Mapper.toTransactionResponse(transaction))
                .product(Mapper.toProductResponse(product))
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
}
