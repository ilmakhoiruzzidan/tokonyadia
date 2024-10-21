package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.response.FileDownloadResponse;
import com.enigma.tokonyadia_api.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constant.API)
public class FileController {
    private final ProductImageService productImageService;

    @GetMapping("/images/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable("id") String id) {
        FileDownloadResponse fileDownloadResponse = productImageService.downloadImage(id);
        String headerValue = String.format("inline; filename=%s", fileDownloadResponse.getResource().getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .contentType(MediaType.parseMediaType(fileDownloadResponse.getContentType()))
                .body(fileDownloadResponse.getResource());
    }
}
