package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    Customer getCustomerById(String id);
    List<Customer> getAllCustomers();
    String deleteCustomer(String id);
    Customer updateCustomer(String id,Customer customer);

}
