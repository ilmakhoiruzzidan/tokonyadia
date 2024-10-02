package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.repository.ProductRepository;
import com.enigma.tokonyadia_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(String id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new RuntimeException("Data produk tidak ditemukan");
        }
        return byId.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public String deleteProduct(String id) {
        Product productById = getProductById(id);
        if (productById != null) {
            productRepository.delete(productById);
            return "Product berhasil dihapus";
        } else {
            throw new RuntimeException("Data produk tidak ada");
        }
    }

    @Override
    public Product updateProduct(String id, Product product) {
        Optional<Product> selectedProduct = productRepository.findById(id);
        if (selectedProduct.isPresent()) {
            Product newProduct = selectedProduct.get();
            newProduct.setName(product.getName());
            newProduct.setPrice(product.getPrice());
            newProduct.setStore(product.getStore());
            return productRepository.save(newProduct);
        }
        throw new RuntimeException("Update data gagal");
    }
}
