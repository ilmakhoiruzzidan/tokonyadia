package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.service.TransactionService;
import com.enigma.tokonyadia_api.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.RequestContextFilter;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final RequestContextFilter requestContextFilter;

    @PostMapping
    public ResponseEntity<?> createTransaction (@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, "OK", transactionResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions (
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        PagingAndSortingRequest pagingAndSortingRequest = PagingAndSortingRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();
        Page<Transaction> transactionPage = transactionService.getAllTransactions(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, "OK", transactionPage);
    }
}
