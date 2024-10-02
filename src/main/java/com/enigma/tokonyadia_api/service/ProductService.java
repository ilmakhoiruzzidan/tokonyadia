package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(String id);
    List<Product> getAllProducts();
    void deleteProduct(String id);
    Product updateProduct(String id, Product product);
}
