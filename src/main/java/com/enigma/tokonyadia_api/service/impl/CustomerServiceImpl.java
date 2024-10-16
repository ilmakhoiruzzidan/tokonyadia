package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.CustomerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.repository.CustomerRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.service.UserService;
import com.enigma.tokonyadia_api.specification.CustomerSpecification;
import com.enigma.tokonyadia_api.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        userService.create(userAccount);

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userAccount(userAccount)
                .build();
        customerRepository.saveAndFlush(customer);
        return Mapper.toCustomerResponse(customer);
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = getOne(id);
        return Mapper.toCustomerResponse(customer);
    }

    @Override
    public Page<CustomerResponse> getAllCustomers(SearchCustomerRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Customer> customerSpecification = CustomerSpecification.getSpecification(request);
        Page<Customer> customerPage = customerRepository.findAll(customerSpecification, pageable);
        return customerPage.map(Mapper::toCustomerResponse);
    }

    @Override
    public CustomerResponse updateCustomer(String id, CustomerRequest request) {
        Customer newCustomer = getOne(id);
        newCustomer.setName(request.getName());
        newCustomer.setEmail(request.getEmail());
        newCustomer.setPhoneNumber(request.getPhoneNumber());
        customerRepository.save(newCustomer);
        return Mapper.toCustomerResponse(newCustomer);
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer = getOne(id);
        if (customer == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_CUSTOMER_NOT_FOUND);
        customerRepository.deleteById(id);

    }

    @Override
    public Customer getOne(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_CUSTOMER_NOT_FOUND));
    }

}
