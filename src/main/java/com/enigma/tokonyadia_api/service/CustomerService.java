package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomerById(String id);

    Page<CustomerResponse> getAllCustomers(Integer page, Integer size, String sort);

    void deleteCustomer(String id);

    CustomerResponse updateCustomer(String id, CustomerRequest request);

}
