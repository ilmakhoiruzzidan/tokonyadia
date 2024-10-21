package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(List<MultipartFile> multipartFiles, ProductRequest request);

    ProductResponse getProductById(String id);

    Page<ProductResponse> getAllProducts(SearchProductRequest request);

    Page<?> getAllProductByStore(SearchProductRequest request);

    void deleteProduct(String id);

    ProductResponse updateProductAndImage(List<MultipartFile> multipartFiles, ProductRequest request, String id);
    ProductResponse updateImage(MultipartFile file, String imageId);
    Product updateProductAndImage(Product product);
    Product getOne(String id);
}
