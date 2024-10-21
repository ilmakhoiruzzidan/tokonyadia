package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ProductController {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        ProductResponse productById = productService.getProductById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT_BY_ID, productById);
    }

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
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @PatchMapping(path = "/images/{id}")
    public ResponseEntity<?> updateSpecifiedImageById(
            @PathVariable String id,
            @RequestParam(name = "image") MultipartFile file
    ) {
        ProductResponse productResponse = productService.updateImage(file, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT_IMAGE, productResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @DeleteMapping(path = "/images/{id}")
    public ResponseEntity<?> deleteSpecifiedImageById(@PathVariable String id) {
        productService.deleteImage(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT_IMAGE, null);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT, null);
    }
}
