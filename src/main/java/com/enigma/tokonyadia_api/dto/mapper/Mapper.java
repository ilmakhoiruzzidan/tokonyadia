package com.enigma.tokonyadia_api.dto.mapper;

import com.enigma.tokonyadia_api.dto.response.*;
import com.enigma.tokonyadia_api.entity.*;

public class Mapper {
    public static Customer toCustomer(CustomerResponse customerResponse) {
        return Customer.builder()
                .id(customerResponse.getId())
                .name(customerResponse.getName())
                .email(customerResponse.getEmail())
                .phoneNumber(customerResponse.getPhoneNumber())
                .build();
    }

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }


    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .store(toStoreResponse(product.getStore()))
                .description(product.getDescription())
                .stock(product.getStock())
                .build();
    }

    public static Product toProduct(ProductResponse productResponse) {
        return Product.builder()
                .id(productResponse.getId())
                .name(productResponse.getName())
                .description(productResponse.getDescription())
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

    public static Store toStore(StoreResponse storeResponse) {
        if (storeResponse == null) {
            throw new IllegalArgumentException("StoreResponse tidak boleh null");
        }
        return Store.builder()
                .id(storeResponse.getId())
                .name(storeResponse.getName())
                .phoneNumber(storeResponse.getPhoneNumber())
                .address(storeResponse.getAddress())
                .noSiup(storeResponse.getNoSiup())
                .build();
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction){
        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(transaction.getCustomer().getId())
                .name(transaction.getCustomer().getName())
                .email(transaction.getCustomer().getEmail())
                .phoneNumber(transaction.getCustomer().getPhoneNumber())
                .build();
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .transactionDate(transaction.getTransDate())
                .customer(customerResponse)
                .build();
    }

    public static TransactionDetailResponse toTransactionDetailResponse (TransactionDetail transactionDetail){
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
}
