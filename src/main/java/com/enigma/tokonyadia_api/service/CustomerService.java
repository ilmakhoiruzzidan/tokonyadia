package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.CustomerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse create(CustomerCreateRequest request);

    Customer create(Customer customer);

    CustomerResponse getCustomerById(String id);

    Page<CustomerResponse> getAllCustomers(SearchCustomerRequest request);

    CustomerResponse updateCustomer(String id, CustomerRequest request);

    void deleteCustomer(String id);

    Customer getOne(String id);
}
