package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.CategoryRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.CategoryResponse;
import com.enigma.tokonyadia_api.service.CategoryService;
import com.enigma.tokonyadia_api.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.CATEGORY_API)
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        PagingAndSortingRequest pagingAndSortingRequest = PagingAndSortingRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();

        Page<CategoryResponse> categoryResponses = categoryService.getAll(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, "Successfully retrieve all category", categoryResponses);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse categoryResponse = categoryService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Successfully create category", categoryResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        CategoryResponse categoryResponse = categoryService.update(request, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully update category", categoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully delete category", null);
    }

}
