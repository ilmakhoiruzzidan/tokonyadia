package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import com.enigma.tokonyadia_api.service.ProductCategoryService;
import com.enigma.tokonyadia_api.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.PRODUCTS_CATEGORIES)
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

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

        Page<ProductCategoryResponse> productCategoryResponses = productCategoryService.getAll(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, "Successfully retrieve all category", productCategoryResponses);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Successfully create category", productCategoryResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.update(request, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully update category", productCategoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        productCategoryService.delete(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully delete category", null);
    }

}
