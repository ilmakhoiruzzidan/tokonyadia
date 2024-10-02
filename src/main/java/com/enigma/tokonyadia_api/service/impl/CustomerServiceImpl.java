package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.repository.CustomerRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    public final CustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isEmpty()) {
            throw new RuntimeException("Data pelanggan tidak ditemukan");
        }
        return byId.get();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customerById = getCustomerById(id);
        if (customerById != null) {
            customerRepository.deleteById(id);
        }
        throw new RuntimeException("Data pelanggan tidak ada");
    }

    @Override
    public Customer updateCustomer(String id, Customer customer) {
        Optional<Customer> selectedCustomer = customerRepository.findById(id);
        if(selectedCustomer.isPresent()) {
            Customer newCustomer = selectedCustomer.get();
            newCustomer.setName(customer.getName());
            newCustomer.setEmail(customer.getEmail());
            newCustomer.setPhoneNumber(customer.getPhoneNumber());
            return customerRepository.save(newCustomer);
        }
        throw new RuntimeException("Update data gagal");
    }
}
