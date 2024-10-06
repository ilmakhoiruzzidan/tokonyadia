package com.enigma.tokonyadia_api.dto.mapper;

import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;

public class CustomerMapper {
    public static Customer toCustomer(CustomerResponse customerResponse) {
        return Customer.builder()
                .id(customerResponse.getId())
                .name(customerResponse.getName())
                .email(customerResponse.getEmail())
                .phoneNumber(customerResponse.getPhoneNumber())
                .build();
    }
}
