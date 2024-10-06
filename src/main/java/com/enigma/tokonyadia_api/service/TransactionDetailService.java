package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
    List<TransactionDetail> addTransactionDetail(Transaction transaction, List<TransactionDetailRequest> transactionDetailRequests);
}
