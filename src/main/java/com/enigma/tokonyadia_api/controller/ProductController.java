package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = Constant.PRODUCT_API)
@RestController
@RequiredArgsConstructor
public class ProductController {
    public final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.createProduct(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_PRODUCT, productResponse);
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
        Page<ProductResponse> allProducts = productService.getAllProducts(searchProductRequest);

        return ResponseUtil.buildResponsePagination(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PRODUCT, allProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        ProductResponse productById = productService.getProductById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT_BY_ID, productById);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.updateProduct(request, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT, productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT, null);
    }
}
