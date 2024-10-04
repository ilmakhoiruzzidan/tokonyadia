package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomerById(String id);

    Page<CustomerResponse> getAllCustomers(PagingAndSortingRequest request);

    void deleteCustomer(String id);

    Page<CustomerResponse> findCustomerByName(SearchCustomerRequest request);

    CustomerResponse updateCustomer(String id, CustomerRequest request);
}
