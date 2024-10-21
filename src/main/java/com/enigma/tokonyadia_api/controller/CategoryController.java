package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import com.enigma.tokonyadia_api.service.ProductCategoryService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.PRODUCT_CATEGORIES_API)
@RequiredArgsConstructor
@Tag(name = "Product Categories", description = "APIs for managing product categories")
public class CategoryController {

    private final ProductCategoryService productCategoryService;

    @Operation(summary = "Get all product categories",
            description = "Retrieve a paginated list of all product categories. Optionally sort the results.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                            content = @Content(schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @GetMapping
    public ResponseEntity<?> getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        PagingAndSortingRequest pagingAndSortingRequest = PagingAndSortingRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();

        Page<ProductCategoryResponse> productCategoryResponses = productCategoryService.getAll(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, "Successfully retrieve all category", productCategoryResponses);
    }

    @Operation(summary = "Create a new product category",
            description = "Create a new product category in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully",
                            content = @Content(schema = @Schema(implementation = ProductCategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid category data",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Successfully create category", productCategoryResponse);
    }

    @Operation(summary = "Update an existing product category",
            description = "Update the details of an existing product category by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully",
                            content = @Content(schema = @Schema(implementation = ProductCategoryResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid category data",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.update(request, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully update category", productCategoryResponse);
    }

    @Operation(summary = "Delete a product category",
            description = "Delete a product category from the system by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        productCategoryService.delete(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully delete category", null);
    }
}
