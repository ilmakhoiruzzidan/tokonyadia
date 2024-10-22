package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.dto.request.CustomerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.repository.CustomerRepository;
import com.enigma.tokonyadia_api.service.UserService;
import com.enigma.tokonyadia_api.util.SortUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    CustomerServiceImpl customerService;


    @Test
    void shouldReturnCustomerResponseWhileCreateCustomer() {
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setName("name");
        request.setEmail("email@email.com");
        request.setPassword("password");
        request.setPhoneNumber("phoneNumber");

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("username");
        userAccount.setPassword("password");
        userAccount.setRole(UserRole.ROLE_CUSTOMER);

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setUserAccount(userAccount);
        Mockito.when(userService.create(Mockito.any(UserAccount.class))).thenReturn(userAccount);

        CustomerResponse actualResponse = customerService.create(request);

        assertEquals(request.getName(), actualResponse.getName());
        assertEquals(request.getEmail(), actualResponse.getEmail());
        assertEquals(request.getPhoneNumber(), actualResponse.getPhoneNumber());
    }

    @Test
    void shouldReturnCustomerWhileCreateCustomer() {

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("username");
        userAccount.setPassword("password");
        userAccount.setRole(UserRole.ROLE_CUSTOMER);

        Customer customer = new Customer();
        customer.setName("name");
        customer.setEmail("email@email.com");
        customer.setPhoneNumber("231321");
        customer.setUserAccount(userAccount);

        Mockito.when(customerRepository.saveAndFlush(customer)).thenReturn(customer);

        Customer actualCustomer = customerService.create(customer);
        assertEquals(customer.getEmail(), actualCustomer.getEmail());
    }

    @Test
    void shouldReturnAllCustomersSuccessfully() {
        // Arrange
        SearchCustomerRequest request = new SearchCustomerRequest();
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("name,asc");

        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);

        // Mock customer data
        Customer customer = new Customer();
        customer.setId("customer123");
        customer.setName("John Doe");
        customer.setEmail("john@example.com");

        // Mocking the Page of customers
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer), pageable, 1);

        // Mocking the behavior of the customerRepository
        Mockito.when(customerRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(customerPage);

        // Mocking the MapperUtil to return CustomerResponse
        CustomerResponse expectedResponse = new CustomerResponse();
        expectedResponse.setId(customer.getId());
        expectedResponse.setName(customer.getName());
        expectedResponse.setEmail(customer.getEmail());

        // Act
        Page<CustomerResponse> actualResponse = customerService.getAllCustomers(request);

        // Assert
        assertEquals(1, actualResponse.getTotalElements());
        assertEquals(expectedResponse.getId(), actualResponse.getContent().get(0).getId());
        assertEquals(expectedResponse.getName(), actualResponse.getContent().get(0).getName());
        assertEquals(expectedResponse.getEmail(), actualResponse.getContent().get(0).getEmail());
        Mockito.verify(customerRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        // Arrange
        String customerId = "customer123";
        CustomerRequest request = new CustomerRequest();
        request.setName("Updated Name");
        request.setEmail("updated@example.com");
        request.setPhoneNumber("123456789");

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setName("Old Name");
        existingCustomer.setEmail("old@example.com");
        existingCustomer.setPhoneNumber("987654321");

        // Mocking the behavior of getOne method to return the existing customer
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Mocking the behavior of saving the updated customer
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(existingCustomer);

        // Mocking the MapperUtil to return CustomerResponse
        CustomerResponse expectedResponse = new CustomerResponse();
        expectedResponse.setId(existingCustomer.getId());
        expectedResponse.setName(request.getName());
        expectedResponse.setEmail(request.getEmail());
        expectedResponse.setPhoneNumber(request.getPhoneNumber());


        // Act
        CustomerResponse actualResponse = customerService.updateCustomer(customerId, request);

        // Assert
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        assertEquals(expectedResponse.getPhoneNumber(), actualResponse.getPhoneNumber());

        // Verify interactions with the repository
        Mockito.verify(customerRepository, Mockito.times(1)).findById(customerId);
        Mockito.verify(customerRepository, Mockito.times(1)).save(existingCustomer);
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        // Arrange
        String customerId = "customer123";
        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);

        // Mocking the behavior of getOne method to return the existing customer
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act
        customerService.deleteCustomer(customerId);

        // Assert
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(customerId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCustomerNotFound() {
        // Arrange
        String customerId = "nonExistentCustomer";

        // Mocking the behavior of getOne method to return empty Optional
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.deleteCustomer(customerId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(Constant.ERROR_CUSTOMER_NOT_FOUND, exception.getReason());

        // Verify interaction with the repository
        Mockito.verify(customerRepository, Mockito.times(0)).deleteById(customerId);
    }


}