package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request);
    Transaction getTransactionById(String transactionId);
    Page<Transaction> getAllTransactions(PagingAndSortingRequest request);
}
