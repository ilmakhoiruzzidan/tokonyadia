package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.ProductRepository;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.utils.SortUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final StoreService storeService;

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        StoreResponse storeResponse = storeService.getStoreById(request.getStoreId());
        Store store = convertToStore(storeResponse);
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .store(store)
                .build();
        productRepository.saveAndFlush(product);
        return toProductResponse(product);
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = getOne(id);
        if (product != null) {
            return toProductResponse(product);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produk tidak ditemukan");
    }

    @Override
    public Page<ProductResponse> getAllProducts(Integer page, Integer size, String sort) {
        if (page <= 0) page = 1;
        Sort sortBy = SortUtil.parseSort(sort);
        Pageable pageable = PageRequest.of((page - 1), size, sortBy);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(new Function<Product, ProductResponse>() {
            @Override
            public ProductResponse apply(Product product) {
                return toProductResponse(product);
            }
        });
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

    @Override
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product newProduct = getOne(id);
        StoreResponse storeResponse = storeService.getStoreById(request.getStoreId());
        Store store = convertToStore(storeResponse);
        if (newProduct != null) {
            newProduct.setName(request.getName());
            newProduct.setPrice(request.getPrice());
            newProduct.setStore(store);
            productRepository.save(newProduct);
            return toProductResponse(newProduct);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gagal melakukan update");
    }

    public Product getOne(String id) {
        Optional<Product> byId = productRepository.findById(id);
        return byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produk tidak ditemukan"));
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .storeId(product.getStore().getId())
                .build();
    }

    private Store convertToStore(StoreResponse storeResponse) {
        if (storeResponse == null) {
            throw new IllegalArgumentException("StoreResponse tidak boleh null");
        }
        return Store.builder()
                .id(storeResponse.getId())
                .name(storeResponse.getName())
                .phoneNumber(storeResponse.getPhoneNumber())
                .address(storeResponse.getPhoneNumber())
                .noSiup(storeResponse.getNoSiup())
                .build();
    }

}
