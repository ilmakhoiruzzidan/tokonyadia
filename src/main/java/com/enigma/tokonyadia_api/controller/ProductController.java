package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping(path = Constant.PRODUCT_API)
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product Management", description = "Operations related to managing products")
public class ProductController {
    private static class CommonResponseProductResponse extends CommonResponse<ProductResponse> {
    }

    private static class CommonResponseListProductResponse extends CommonResponse<List<ProductResponse>> {
    }
    private final ProductService productService;
    private final ObjectMapper objectMapper;


    @Operation(summary = "Create a new product",
            description = "This endpoint allows an admin to create a new product item. Images can be optionally uploaded with the product details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestParam(name = "images", required = false) List<MultipartFile> images,
                                           @RequestParam(name = "product") String product) {
        try {
            ProductRequest productRequest = objectMapper.readValue(product, ProductRequest.class);
            ProductResponse productResponse = productService.createProduct(images, productRequest);
            return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_PRODUCT, productResponse);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(summary = "Retrieve all products",
            description = "This endpoint retrieves a paginated list of all available products, with optional filtering and sorting.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = CommonResponseListProductResponse.class))),
            })
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        SearchProductRequest searchProductRequest = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .query(query)
                .sortBy(sortBy)
                .build();
        Page<ProductResponse> productResponses = productService.getAllProducts(searchProductRequest);

        return ResponseUtil.buildResponsePagination(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PRODUCT, productResponses);
    }

    @Operation(summary = "Get product by ID",
            description = "Retrieve details of a specific product item by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product details retrieved successfully", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        ProductResponse productById = productService.getProductById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT_BY_ID, productById);
    }

    @Operation(summary = "Update product",
            description = "Update the details of an existing product item by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id,
                                           @RequestParam List<MultipartFile> images,
                                           @RequestParam String product) {
        try {
            ProductRequest productRequest = objectMapper.readValue(product, ProductRequest.class);
            ProductResponse productResponse = productService.updateProductAndImage(images, productRequest, id);
            return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT, productResponse);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @GetMapping("/stores")
    public ResponseEntity<?> getProductByStores(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "store", required = false) String store
    ) {
        SearchProductRequest searchProductRequest = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .query(query)
                .sortBy(sortBy)
                .store(store)
                .build();
        Page<?> productResponses = productService.getAllProductByStore(searchProductRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PRODUCT, productResponses);
    }

    @Operation(summary = "Update specific product image",
            description = "Updates a specific image associated with a product. Requires image ID and the new image file to be uploaded.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product image updated successfully", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product image not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PatchMapping(path = "/images/{id}")
    public ResponseEntity<?> updateSpecifiedImageById(
            @PathVariable String id,
            @RequestParam(name = "image") MultipartFile file
    ) {
        ProductResponse productResponse = productService.updateImage(file, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT_IMAGE, productResponse);
    }

    @Operation(summary = "Delete specific product image",
            description = "Deletes a specific image associated with a product using the image ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product image deleted successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product image not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping(path = "/images/{id}")
    public ResponseEntity<?> deleteSpecifiedImageById(@PathVariable String id) {
        productService.deleteImage(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT_IMAGE, null);
    }

    @Operation(summary = "Delete product",
            description = "Delete a specific product item by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product deleted successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT, null);
    }
}
