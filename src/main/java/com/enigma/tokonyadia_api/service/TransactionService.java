package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.DraftTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionResponse createDraft(DraftTransactionRequest request);
    List<TransactionDetailResponse> getTransactionDetails(String transactionId);
    TransactionResponse getTransactionById(String transactionId);
    Transaction getOne(String transactionId);
    Page<TransactionResponse> getAllTransactions(PagingAndSortingRequest request);
    TransactionResponse addTransactionDetail(String transactionId, TransactionDetailRequest request);
    TransactionResponse updateTransactionDetails(String transactionId, String detailsId, TransactionDetailRequest request);
    TransactionResponse deleteTransactionDetails(String transactionId, String detailsId);
}
