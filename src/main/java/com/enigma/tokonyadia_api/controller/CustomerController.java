package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.CustomerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.CustomerRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = Constant.CUSTOMER_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Customer Management", description = "Operations related to managing customer data, including creation, update, retrieval, and deletion")
public class CustomerController {
    private static class CommonResponseCustomerResponse extends CommonResponse<CustomerResponse> {}
    private static class CommonResponseListCustomerResponse extends CommonResponse<List<CustomerResponse>> {}
    private final CustomerService customerService;

    @Operation(summary = "Create a new customer",
            description = "This endpoint allows the creation of a new customer. The customer data is passed in the request body.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = CommonResponseCustomerResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerCreateRequest request) {
        CustomerResponse customerResponse = customerService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_CUSTOMER, customerResponse);
    }

    @Operation(summary = "Retrieve all customers",
            description = "Retrieve a paginated list of all customers. Optional query parameters include pagination and sorting.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully", content = @Content(schema = @Schema(implementation = CommonResponseListCustomerResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
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

    @Operation(summary = "Get customer by ID",
            description = "Retrieve the details of a specific customer by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer details retrieved successfully", content = @Content(schema = @Schema(implementation = CommonResponseCustomerResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getCustomerById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_CUSTOMER_BY_ID, customerResponse);
    }

    @Operation(summary = "Update customer details",
            description = "Update the details of a specific customer by their ID. Authorization is required for admin users or customers with appropriate access.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(schema = @Schema(implementation = CommonResponseCustomerResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable String id, @RequestBody CustomerRequest request) {
        CustomerResponse customerResponse = customerService.updateCustomer(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_CUSTOMER, customerResponse);
    }

    @Operation(summary = "Delete customer by ID",
            description = "Delete a specific customer by their ID. Only authorized users can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer deleted successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_CUSTOMER, null);
    }
}
