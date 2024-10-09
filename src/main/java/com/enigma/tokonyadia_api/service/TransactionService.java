package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.DraftTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionRequest;
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
    Page<Transaction> getAllTransactions(PagingAndSortingRequest request);
    TransactionResponse updateTransaction(String transactionId, TransactionRequest request);
    TransactionResponse deleteTransaction(String transactionId);
}
