package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.CustomerRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.specification.CustomerSpecification;
import com.enigma.tokonyadia_api.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    public final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
        customerRepository.saveAndFlush(customer);
        return toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = getOne(id);
        return toCustomerResponse(customer);
    }

    @Override
    public Page<CustomerResponse> getAllCustomers(SearchCustomerRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Customer> customerSpecification = CustomerSpecification.getSpecification(request);
        Page<Customer> customerPage = customerRepository.findAll(customerSpecification, pageable);
        return customerPage.map(new Function<Customer, CustomerResponse>() {
            @Override
            public CustomerResponse apply(Customer customer) {
                return toCustomerResponse(customer);
            }
        });

    }

    @Override
    public CustomerResponse updateCustomer(String id, CustomerRequest request) {
        Customer newCustomer = getOne(id);
        if (newCustomer != null) {
            newCustomer.setName(request.getName());
            newCustomer.setEmail(request.getEmail());
            newCustomer.setPhoneNumber(request.getPhoneNumber());
            customerRepository.save(newCustomer);
            return toCustomerResponse(newCustomer);
        }
        throw new RuntimeException("Update data gagal");
    }


    @Override
    public void deleteCustomer(String id) {
        Customer customer = getOne(id);
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pelanggan tidak ditemukan");
        } else {
            customerRepository.deleteById(id);
        }
    }


    @Override
    public Customer getOne(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        return byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pelanggan tidak ditemukan"));
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .build();
    }
}
