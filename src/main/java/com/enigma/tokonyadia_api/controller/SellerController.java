package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.SearchSellerRequest;
import com.enigma.tokonyadia_api.dto.request.SellerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.SellerRequest;
import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.SellerResponse;
import com.enigma.tokonyadia_api.service.SellerService;
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
@RequestMapping(path = Constant.SELLER_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Seller Management", description = "APIs for managing seller data, including creation, update, retrieval, and deletion")
public class SellerController {

    private static class CommonResponseSellerResponse extends CommonResponse<SellerResponse> {
    }

    private static class CommonResponseListSellerResponse extends CommonResponse<List<SellerResponse>> {
    }

    private final SellerService sellerService;

    @Operation(summary = "Create a new seller",
            description = "This endpoint allows the creation of a new seller. The seller data is passed in the request body.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Seller created successfully", content = @Content(schema = @Schema(implementation = SellerController.CommonResponseSellerResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(SellerCreateRequest request) {
        SellerResponse sellerResponse = sellerService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_SELLER, sellerResponse);
    }

    @Operation(summary = "Get seller by ID",
            description = "Retrieve the details of a specific seller by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Seller details retrieved successfully", content = @Content(schema = @Schema(implementation = SellerController.CommonResponseSellerResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Seller not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSellerById(@PathVariable String id) {
        SellerResponse sellerResponse = sellerService.getSellerById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully retrieve seller", sellerResponse);
    }

    @Operation(summary = "Retrieve all sellers",
            description = "Retrieve a paginated list of all sellers. Optional query parameters include pagination and sorting.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sellers retrieved successfully", content = @Content(schema = @Schema(implementation = SellerController.CommonResponseListSellerResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping
    public ResponseEntity<?> getAllSeller(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        SearchSellerRequest searchSellerRequest = SearchSellerRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .sortBy(sortBy)
                .build();
        Page<SellerResponse> sellerResponses = sellerService.getAllSellers(searchSellerRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_ALL_SELLER, sellerResponses);
    }

    @Operation(summary = "Update seller details",
            description = "Update the details of a specific seller by their ID. Authorization is required for admin users or sellers with appropriate access.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Seller updated successfully", content = @Content(schema = @Schema(implementation = SellerController.CommonResponseSellerResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Seller not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SELLER') and @permissionEvaluationServiceImpl.hasAccessToSeller(#id, authentication.principal.id))")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeller(@PathVariable String id, @RequestBody SellerRequest request) {
        SellerResponse sellerResponse = sellerService.updateSeller(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_SELLER, sellerResponse);
    }

    @Operation(summary = "Delete seller by ID",
            description = "Delete a specific seller by their ID. Only authorized users can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Seller deleted successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Seller not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable String id) {
        sellerService.deleteSeller(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_SELLER, null);
    }
}
