package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.CategoryResponse;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Category;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.ProductRepository;
import com.enigma.tokonyadia_api.service.CategoryService;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.specification.ProductSpecification;
import com.enigma.tokonyadia_api.utils.SortUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final StoreService storeService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Store store = storeService.getOne(request.getStoreId());
        Category category = categoryService.getOne(request.getCategoryId());
        Product product = Product.builder()
                .store(store)
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .stock(request.getStock())
                .category(category)
                .build();
        productRepository.saveAndFlush(product);
        return Mapper.toProductResponse(product);
    }

    @Override
    public Product getOne(String productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = getOne(id);
        return Mapper.toProductResponse(product);
    }


    @Override
    public Page<ProductResponse> getAllProducts(SearchProductRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Product> productSpecification = ProductSpecification.getSpecification(request);
        Page<Product> productPage = productRepository.findAll(productSpecification, pageable);
        return productPage.map(Mapper::toProductResponse);
    }

    @Override
    public ProductResponse updateProduct(ProductRequest request, String id) {
        Product newProduct = getOne(id);
        newProduct.setName(request.getName());
        newProduct.setPrice(request.getPrice());
        productRepository.save(newProduct);
        return Mapper.toProductResponse(newProduct);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = getOne(id);
        productRepository.delete(product);

    }

}
