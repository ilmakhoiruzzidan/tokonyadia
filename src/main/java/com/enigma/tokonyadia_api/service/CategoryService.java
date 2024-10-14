package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.CategoryRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.CategoryResponse;
import com.enigma.tokonyadia_api.entity.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);

    Page<CategoryResponse> getAll(PagingAndSortingRequest request);

    CategoryResponse update(CategoryRequest request, String id);

    void delete(String id);

    Category getOne(String id);
}
