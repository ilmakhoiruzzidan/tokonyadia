package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.DraftTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.service.TransactionService;
import com.enigma.tokonyadia_api.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/draft")
    public ResponseEntity<?> createDraft(@RequestBody DraftTransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.createDraft(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "OK", transactionResponse);
    }

    @GetMapping("/{transactionId}/details")
    public ResponseEntity<?> getTransactionDetails(@PathVariable String transactionId) {
        List<TransactionDetailResponse> transactionDetailResponses = transactionService.getTransactionDetails(transactionId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_TRANSACTION_DETAIL, transactionDetailResponses);
    }

    @PostMapping("/{transactionId}/details")
    public ResponseEntity<?> addTransactionToDetails(@PathVariable String transactionId, @Valid @RequestBody TransactionDetailRequest request) {
        TransactionResponse transactionResponse = transactionService.addTransactionDetail(transactionId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully add transaction details", transactionResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        PagingAndSortingRequest pagingAndSortingRequest = PagingAndSortingRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();
        Page<TransactionResponse> transactionPage = transactionService.getAllTransactions(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, "OK", transactionPage);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransactionById(@PathVariable String transactionId) {
        TransactionResponse transactionResponse = transactionService.getTransactionById(transactionId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_TRANSACTION_BY_ID, transactionResponse);
    }

    @PutMapping("/{transactionId}/details/{detailsId}")
    public ResponseEntity<?> updateTransactionDetail(@PathVariable String transactionId, @PathVariable String detailsId, @Valid @RequestBody TransactionDetailRequest transactionDetailRequest) {
        TransactionResponse transactionResponse = transactionService.updateTransactionDetails(transactionId, detailsId, transactionDetailRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_TRANSACTION, transactionResponse);
    }

    @DeleteMapping("/{transactionId}/details/{detailsId}")
    public ResponseEntity<?> deleteTransactionById(@PathVariable String transactionId, @PathVariable String detailsId) {
        transactionService.deleteTransactionDetails(transactionId, detailsId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_REMOVE_TRANSACTION_DETAIL, null);
    }
}
