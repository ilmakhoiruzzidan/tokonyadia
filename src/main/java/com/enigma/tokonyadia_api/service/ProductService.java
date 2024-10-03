package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(String id);
    Page<ProductResponse> getAllProducts(Integer page, Integer size, String sort);
    void deleteProduct(String id);
    ProductResponse updateProduct(String id, ProductRequest request);
}
