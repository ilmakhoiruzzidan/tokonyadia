package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import com.enigma.tokonyadia_api.entity.Category;
import org.springframework.data.domain.Page;

public interface ProductCategoryService {
    ProductCategoryResponse create(ProductCategoryRequest request);

    Page<ProductCategoryResponse> getAll(PagingAndSortingRequest request);

    ProductCategoryResponse update(ProductCategoryRequest request, String id);

    void delete(String id);

    Category getOne(String id);
}
