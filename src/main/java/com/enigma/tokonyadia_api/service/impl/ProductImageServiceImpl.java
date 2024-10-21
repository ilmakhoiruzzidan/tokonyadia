package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.FileType;
import com.enigma.tokonyadia_api.dto.response.FileDownloadResponse;
import com.enigma.tokonyadia_api.dto.response.FileInfo;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.ProductImage;
import com.enigma.tokonyadia_api.repository.ProductImageRepository;
import com.enigma.tokonyadia_api.service.FileStorageService;
import com.enigma.tokonyadia_api.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final FileStorageService fileStorageService;
    private final List<String> contentTypes = List.of("image/jpg", "image/png", "image/webp", "image/jpeg");
    private final String PRODUCT = "product";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductImage create(MultipartFile multipartFile, Product product) {
        FileInfo fileInfo = fileStorageService.storeFile(FileType.IMAGE, PRODUCT, multipartFile, contentTypes);
        ProductImage productImage = ProductImage.builder()
                .filename(fileInfo.getFilename())
                .path(fileInfo.getPath())
                .contentType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .product(product)
                .build();
        return productImageRepository.saveAndFlush(productImage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProductImage> createBulk(List<MultipartFile> multipartFiles, Product product) {
        return multipartFiles.stream().map(multipartFile -> create(multipartFile, product)).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductImage update(String imageId, MultipartFile multipartFile) {
        ProductImage productImage = findByIdOrThrowNotFound(imageId);
        FileInfo fileInfo = fileStorageService.storeFile(FileType.IMAGE, PRODUCT, multipartFile, contentTypes);
        fileStorageService.deleteFile(productImage.getPath());
        productImage.setPath(fileInfo.getPath());
        return productImageRepository.saveAndFlush(productImage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String imageId) {
        ProductImage productImage = findByIdOrThrowNotFound(imageId);
        String path = productImage.getPath();
        productImageRepository.delete(productImage);
        fileStorageService.deleteFile(path);
    }

    @Transactional(readOnly = true)
    @Override
    public FileDownloadResponse downloadImage(String imageId) {
        ProductImage productImage = findByIdOrThrowNotFound(imageId);
        Resource resource = fileStorageService.readFile(productImage.getPath());
        return FileDownloadResponse.builder()
                .resource(resource)
                .contentType(productImage.getContentType())
                .build();
    }

    @Transactional(readOnly = true)
    public ProductImage findByIdOrThrowNotFound(String id) {
        return productImageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "image not found"));
    }
}
