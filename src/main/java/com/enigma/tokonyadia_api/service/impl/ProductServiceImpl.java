package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.dto.response.SimpleProductResponse;
import com.enigma.tokonyadia_api.dto.response.StoreProductResponse;
import com.enigma.tokonyadia_api.entity.Category;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.ProductImage;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.ProductRepository;
import com.enigma.tokonyadia_api.service.ProductCategoryService;
import com.enigma.tokonyadia_api.service.ProductImageService;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.specification.ProductSpecification;
import com.enigma.tokonyadia_api.util.MapperUtil;
import com.enigma.tokonyadia_api.util.SortUtil;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final StoreService storeService;
    private final ProductCategoryService productCategoryService;
    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse createProduct(List<MultipartFile> multipartFiles, ProductRequest request) {
        validationUtil.validate(request);
        Store store = storeService.getOne(request.getStoreId());
        Category category = productCategoryService.getOne(request.getCategoryId());
        Product product = Product.builder()
                .store(store)
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .stock(request.getStock())
                .category(category)
                .build();
        productRepository.saveAndFlush(product);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<ProductImage> productImages = productImageService.createBulk(multipartFiles, product);
            product.setImages(productImages);
        }
        return MapperUtil.toProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getOne(String productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_PRODUCT_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductById(String id) {
        Product product = getOne(id);
        return MapperUtil.toProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAllProducts(SearchProductRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Product> productSpecification = ProductSpecification.getSpecification(request);
        Page<Product> productPage = productRepository.findAll(productSpecification, pageable);
        return productPage.map(MapperUtil::toProductResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<?> getAllProductByStore(SearchProductRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Product> productSpecification = ProductSpecification.getSpecification(request);
        Page<Product> productPage = productRepository.findAll(productSpecification, pageable);
        List<StoreProductResponse> newStoreProductResponses = new ArrayList<>();

        if (productPage.isEmpty()) {
            log.warn("No products found");
            return new PageImpl<>(newStoreProductResponses, pageable, productPage.getTotalElements());
        }

        for (Product product : productPage.getContent()) {
            StoreProductResponse storeResponse = newStoreProductResponses.stream()
                    .filter(s -> s.getStoreId().equals(product.getStore().getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        StoreProductResponse storeProductResponse = StoreProductResponse.builder()
                                .storeId(product.getStore().getId())
                                .storeName(product.getStore().getName())
                                .products(new ArrayList<>())
                                .build();
                        newStoreProductResponses.add(storeProductResponse);
                        return storeProductResponse;
                    });

            SimpleProductResponse simpleProductResponse = SimpleProductResponse.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .stock(product.getStock())
                    .price(product.getPrice())
                    .images(MapperUtil.getProductImages(product))
                    .categoryName(product.getCategory().getName())
                    .storeName(product.getStore().getName())
                    .build();

            storeResponse.getProducts().add(simpleProductResponse);
        }
        return new PageImpl<>(newStoreProductResponses, pageable, productPage.getTotalElements());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse updateProductAndImage(List<MultipartFile> multipartFiles, ProductRequest request, String id) {
        Product newProduct = getOne(id);
        newProduct.setName(request.getName());
        newProduct.setStock(request.getStock());
        newProduct.setPrice(request.getPrice());
        newProduct.setDescription(request.getDescription());
        newProduct.setStore(storeService.getOne(request.getStoreId()));
        newProduct.setCategory(productCategoryService.getOne(request.getCategoryId()));

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<ProductImage> productImages = productImageService.createBulk(multipartFiles, newProduct);
            if (newProduct.getImages() != null && !newProduct.getImages().isEmpty()) {
                newProduct.getImages().addAll(productImages);
            } else {
                newProduct.setImages(productImages);
            }
        }

        productRepository.save(newProduct);
        return MapperUtil.toProductResponse(newProduct);
    }

    @Override
    public ProductResponse updateImage(MultipartFile file, String imageId) {
        ProductImage productImage = productImageService.update(imageId, file);
        return MapperUtil.toProductResponse(productImage.getProduct());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Product updateProductAndImage(Product product) {
        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProduct(String id) {
        Product product = getOne(id);
        productRepository.delete(product);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteImage(String imageId) {
        productImageService.deleteById(imageId);
    }

}
