package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.TransactionStatus;
import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.*;
import com.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import com.enigma.tokonyadia_api.repository.TransactionRepository;
import com.enigma.tokonyadia_api.service.*;
import com.enigma.tokonyadia_api.specification.TransactionSpecification;
import com.enigma.tokonyadia_api.utils.SortUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final StoreService storeService;
    private final ProductService productService;
    private final CustomerService customerService;

    @Override
    public TransactionResponse createDraft(DraftTransactionRequest request) {
        Customer customer = customerService.getOne(request.getCustomerId());
        Transaction transaction = Transaction.builder()
                .customer(customer)
                .status(TransactionStatus.DRAFT)
                .transactionDetails(new ArrayList<>())
                .build();
        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        return Mapper.toTransactionResponse(savedTransaction);
    }

    @Override
    public List<TransactionDetailResponse> getTransactionDetails(String transactionId) {
        Transaction transaction = getOne(transactionId);
        return transaction.getTransactionDetails().stream()
                .map(Mapper::toTransactionDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse addTransactionDetail(String transactionId, TransactionDetailRequest request) {
        Transaction transaction = getOne(transactionId);
        if (transaction.getStatus() != TransactionStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only add items to draft transaction");
        }
        Product product = productService.getOne(request.getProductId());

        Optional<TransactionDetail> existingTransactionDetail = transaction.getTransactionDetails().stream()
                .filter(transactionDetail -> transactionDetail.getProduct().getId().equalsIgnoreCase(product.getId()))
                .findFirst();

        if (existingTransactionDetail.isPresent()) {
            TransactionDetail transactionDetail = existingTransactionDetail.get();
            transactionDetail.setQty(transactionDetail.getQty() + request.getQty());
            transactionDetail.setPrice(product.getPrice());
        }

        TransactionDetail.builder()
                .product(product)
                .transaction(transaction)
                .qty(request.getQty())
                .price(product.getPrice())
                .build();

        Transaction updateTransaction = transactionRepository.save(transaction);
        return Mapper.toTransactionResponse(updateTransaction);
    }

    @Override
    public TransactionResponse getTransactionById(String transactionId) {
        Transaction transaction = getOne(transactionId);
        return Mapper.toTransactionResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> getAllTransactions(PagingAndSortingRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Transaction> transactionSpecification = TransactionSpecification.getSpecification(request);
        Page<Transaction> transactionPage = transactionRepository.findAll(transactionSpecification, pageable);
        return transactionPage.map(Mapper::toTransactionResponse);
    }

    @Override
    public TransactionResponse updateTransaction(String transactionId, TransactionRequest request) {
        Transaction transaction = getOne(transactionId);
        // TODO : Update transactionDetail
        return null;
    }

    @Override
    public TransactionResponse deleteTransaction(String transactionId) {
        Transaction transaction = getOne(transactionId);
        return Mapper.toTransactionResponse(transaction);
    }

    @Override
    public Transaction getOne(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_TRANSACTION_NOT_FOUND));
        transaction.getTransactionDetails().size();
        return transaction;
    }
}
