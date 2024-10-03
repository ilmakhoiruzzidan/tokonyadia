package com.enigma.tokonyadia_api.utils;

import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {
    public static <T> ResponseEntity<CommonResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        CommonResponse<T> commonResponse = new CommonResponse<>(status.value(), message, data, null);
        return ResponseEntity.status(status).body(commonResponse);
    }

    public static <T> ResponseEntity<CommonResponse<?>> buildResponsePagination(HttpStatus status, String message, Page<T> page) {
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .page(page.getPageable().getPageNumber() + 1)
                .size(page.getSize())
                .build();
        CommonResponse<List<T>> commonResponse = new CommonResponse<>(status.value(), message, page.getContent(), pagingResponse);
        return ResponseEntity.status(status).body(commonResponse);
    }
}
