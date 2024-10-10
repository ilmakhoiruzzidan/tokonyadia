package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.ProductRepository;
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

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        StoreResponse storeResponse = storeService.getStoreById(request.getStoreId());
        Store store = Mapper.toStore(storeResponse);
        Product product = Product.builder()
                .store(store)
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .stock(request.getStock())
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
        return productPage.map(new Function<Product, ProductResponse>() {
            @Override
            public ProductResponse apply(Product product) {
                return Mapper.toProductResponse(product);
            }
        });
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
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data produk tidak ada");
        } else {
            productRepository.delete(product);
        }
    }

}
