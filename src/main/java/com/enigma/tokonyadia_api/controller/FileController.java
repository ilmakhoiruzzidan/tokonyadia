package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.response.FileDownloadResponse;
import com.enigma.tokonyadia_api.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "File Management", description = "APIs for file handling operations")
public class FileController {
    private final ProductImageService productImageService;

    @Operation(summary = "Download image by ID",
            description = "Download the image file associated with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image downloaded successfully"),
                    @ApiResponse(responseCode = "404", description = "Image not found")
            })
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
