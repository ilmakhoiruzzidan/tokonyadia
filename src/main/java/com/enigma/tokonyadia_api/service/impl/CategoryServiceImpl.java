package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.CategoryRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.CategoryResponse;
import com.enigma.tokonyadia_api.entity.Category;
import com.enigma.tokonyadia_api.repository.CategoryRepository;
import com.enigma.tokonyadia_api.service.CategoryService;
import com.enigma.tokonyadia_api.specification.CategorySpecification;
import com.enigma.tokonyadia_api.utils.SortUtil;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        categoryRepository.save(category);
        return Mapper.toCategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAll(PagingAndSortingRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Category> categorySpecification = CategorySpecification.getSpecification(request);
        Page<Category> categoryPage = categoryRepository.findAll(categorySpecification, pageable);
        return categoryPage.map(Mapper::toCategoryResponse);
    }

    @Override
    public CategoryResponse update(CategoryRequest request, String id) {
        Category category = getOne(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return Mapper.toCategoryResponse(category);
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
