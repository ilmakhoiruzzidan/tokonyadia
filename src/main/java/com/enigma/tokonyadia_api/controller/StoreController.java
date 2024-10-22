package com.enigma.tokonyadia_api.controller;


import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.SearchStoreRequest;
import com.enigma.tokonyadia_api.dto.request.StoreRequest;
import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.service.StoreService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(path = Constant.STORE_API)
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Store Management", description = "APIs for managing store data, including creation, update, retrieval, and deletion")
public class StoreController {
    private static class CommonResponseStoreResponse extends CommonResponse<StoreResponse> {
    }

    private static class CommonResponseListStoreResponse extends CommonResponse<List<StoreResponse>> {
    }

    public final StoreService storeService;

    @Operation(summary = "Create a new store",
            description = "This endpoint allows the creation of a new store. The store data is passed in the request body.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Store created successfully", content = @Content(schema = @Schema(implementation = StoreController.CommonResponseStoreResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping
    public ResponseEntity<?> createStore(@RequestBody StoreRequest request) {
        StoreResponse storeResponse = storeService.createStore(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_STORE, storeResponse);
    }

    @Operation(summary = "Retrieve all stores",
            description = "Retrieve a paginated list of all stores. Optional query parameters include pagination and sorting.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stores retrieved successfully", content = @Content(schema = @Schema(implementation = StoreController.CommonResponseListStoreResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping
    public ResponseEntity<?> getAllStores(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        SearchStoreRequest searchStoreRequest = SearchStoreRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .sortBy(sortBy)
                .build();
        Page<StoreResponse> allStores = storeService.getAllStores(searchStoreRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, Constant.SUCCESS_GET_ALL_STORE, allStores);
    }

    @Operation(summary = "Get store by ID",
            description = "Retrieve the details of a specific store by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store details retrieved successfully", content = @Content(schema = @Schema(implementation = StoreController.CommonResponseStoreResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Store not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable String id) {
        StoreResponse storeResponse = storeService.getStoreById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_STORE_BY_ID, storeResponse);
    }

    @Operation(summary = "Update store details",
            description = "Update the details of a specific store by their ID. Authorization is required for admin users or stores with appropriate access.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store updated successfully", content = @Content(schema = @Schema(implementation = StoreController.CommonResponseStoreResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Store not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStore(@RequestBody StoreRequest request, @PathVariable String id) {
        StoreResponse storeResponse = storeService.updateStore(request, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_STORE, storeResponse);
    }

    @Operation(summary = "Delete store by ID",
            description = "Delete a specific store by their ID. Only authorized users can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store deleted successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Store not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable String id) {
        storeService.deleteStore(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_STORE, null);
    }

}
