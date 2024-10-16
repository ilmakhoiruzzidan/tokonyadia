package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.CustomerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = Constant.CUSTOMER_API)
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerCreateRequest request) {
        CustomerResponse customerResponse = customerService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_CUSTOMER, customerResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        SearchCustomerRequest searchCustomerRequest = SearchCustomerRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .sortBy(sortBy)
                .build();
        Page<CustomerResponse> allCustomers = customerService.getAllCustomers(searchCustomerRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, Constant.SUCCESS_GET_ALL_CUSTOMER, allCustomers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getCustomerById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_CUSTOMER_BY_ID, customerResponse);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @permissionEvaluationServiceImpl.hasAccessToCustomer(#id, authentication.principal.id))")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable String id, @RequestBody CustomerRequest request) {
        CustomerResponse customerResponse = customerService.updateCustomer(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_CUSTOMER, customerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_CUSTOMER, null);
    }

}
