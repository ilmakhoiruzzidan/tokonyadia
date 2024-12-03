package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.util.MapperUtil;
import com.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import com.enigma.tokonyadia_api.entity.Category;
import com.enigma.tokonyadia_api.repository.CategoryRepository;
import com.enigma.tokonyadia_api.service.ProductCategoryService;
import com.enigma.tokonyadia_api.specification.ProductCategorySpecification;
import com.enigma.tokonyadia_api.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public ProductCategoryResponse create(ProductCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        categoryRepository.save(category);
        return MapperUtil.toCategoryResponse(category);
    }

    @Override
    public Page<ProductCategoryResponse> getAll(PagingAndSortingRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Category> categorySpecification = ProductCategorySpecification.getSpecification(request);
        Page<Category> categoryPage = categoryRepository.findAll(categorySpecification, pageable);
        return categoryPage.map(MapperUtil::toCategoryResponse);
    }

    @Override
    public ProductCategoryResponse update(ProductCategoryRequest request, String id) {
        Category newCategory = getOne(id);
        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());
        categoryRepository.save(newCategory);
        return MapperUtil.toCategoryResponse(newCategory);
    }

    @Override
    public void delete(String id) {
        Category category = getOne(id);
        categoryRepository.delete(category);
    }

    @Override
    public Category getOne(String id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_CATEGORY_NOT_FOUND)
        );
    }
}
