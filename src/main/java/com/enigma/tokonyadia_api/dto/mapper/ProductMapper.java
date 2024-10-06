package com.enigma.tokonyadia_api.dto.mapper;

import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.entity.Product;

public class ProductMapper {
    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .store(StoreMapper.toStoreResponse(product.getStore()))
                .description(product.getDescription())
                .stock(product.getStock())
                .build();
    }

    public static Product toProduct(ProductResponse productResponse) {
        return Product.builder()
                .id(productResponse.getId())
                .name(productResponse.getName())
                .description(productResponse.getDescription())
                .build();
    }
}
