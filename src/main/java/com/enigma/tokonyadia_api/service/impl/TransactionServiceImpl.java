package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.mapper.CustomerMapper;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.repository.TransactionRepository;
import com.enigma.tokonyadia_api.service.*;
import com.enigma.tokonyadia_api.specification.TransactionSpecification;
import com.enigma.tokonyadia_api.utils.SortUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Transactional
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final StoreService storeService;
    private final ProductService productService;
    private final CustomerService customerService;

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        CustomerResponse customerResponse = customerService.getCustomerById(request.getCustomerId());
        Customer customer = CustomerMapper.toCustomer(customerResponse);
        Transaction transaction = Transaction.builder()
                .customer(customer)
                .build();
        transactionRepository.saveAndFlush(transaction);
        transactionDetailService.addTransactionDetail(transaction, request.getTransactionDetail());
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .build();
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        return null;
    }

    @Override
    public Page<Transaction> getAllTransactions(PagingAndSortingRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Transaction> transactionSpecification = TransactionSpecification.getSpecification(request);
        Page<Transaction> transactionPage = transactionRepository.findAll(transactionSpecification, pageable);
        return transactionPage.map(new Function<Transaction, Transaction>() {
            @Override
            public Transaction apply(Transaction transaction) {
                return transaction;
            }
        });
    }
}
