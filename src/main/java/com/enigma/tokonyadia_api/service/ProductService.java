package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(String id);

    Page<ProductResponse> getAllProducts(SearchProductRequest request);

    void deleteProduct(String id);

    ProductResponse updateProduct(ProductRequest request, String id);

    Product getProduct(String id);
}
