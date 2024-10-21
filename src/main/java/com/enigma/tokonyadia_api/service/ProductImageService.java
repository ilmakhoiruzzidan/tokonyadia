package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.response.FileDownloadResponse;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    ProductImage create(MultipartFile multipartFile, Product product);
    List<ProductImage> createBulk(List<MultipartFile> multipartFiles, Product product);
    ProductImage update(String imageId, MultipartFile multipartFile);
    void deleteById(String imageId);
    FileDownloadResponse downloadImage(String id);
}
