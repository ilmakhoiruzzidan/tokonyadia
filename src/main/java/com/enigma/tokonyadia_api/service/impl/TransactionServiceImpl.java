package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.TransactionStatus;
import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.DraftTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import com.enigma.tokonyadia_api.repository.TransactionRepository;
import com.enigma.tokonyadia_api.service.*;
import com.enigma.tokonyadia_api.specification.TransactionSpecification;
import com.enigma.tokonyadia_api.util.SortUtil;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final ValidationUtil validationUtil;
    private final TransactionRepository transactionRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createDraft(DraftTransactionRequest request) {
        validationUtil.validate(request);
        Customer customer = customerService.getOne(request.getCustomerId());
        Transaction transaction = Transaction.builder()
                .customer(customer)
                .status(TransactionStatus.DRAFT)
                .transactionDetails(new ArrayList<>())
                .build();
        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        return Mapper.toTransactionResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAllTransactions(PagingAndSortingRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Transaction> transactionSpecification = TransactionSpecification.getSpecification(request);
        Page<Transaction> transactionPage = transactionRepository.findAll(transactionSpecification, pageable);
        return transactionPage.map(Mapper::toTransactionResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionDetailResponse> getTransactionDetails(String transactionId) {
        Transaction transaction = getOne(transactionId);
        return transaction.getTransactionDetails().stream()
                .map(Mapper::toTransactionDetailResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse addTransactionDetail(String transactionId, TransactionDetailRequest request) {
        validationUtil.validate(request);
        Transaction transaction = getOne(transactionId);

        if (transaction.getStatus() != TransactionStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_NON_DRAFT);
        }

        Product product = productService.getOne(request.getProductId());
        if (product.getStock() < request.getQty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_INSUFFICIENT_STOCK);

        boolean isExist = false;
        for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
            if (transactionDetail.getProduct().getId().equalsIgnoreCase(product.getId())) {
                if (transactionDetail.getQty() + request.getQty() > product.getStock()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_INSUFFICIENT_STOCK);
                }
                transactionDetail.setQty(transactionDetail.getQty() + request.getQty());
                transactionDetail.setPrice(product.getPrice());
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            TransactionDetail transactionDetail = TransactionDetail.builder()
                    .product(product)
                    .transaction(transaction)
                    .qty(request.getQty())
                    .price(product.getPrice())
                    .build();
            transaction.getTransactionDetails().add(transactionDetail);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return Mapper.toTransactionResponse(updatedTransaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse updateTransactionDetails(String transactionId, String detailsId, TransactionDetailRequest request) {
        Transaction transaction = getOne(transactionId);
        // TODO : Update transactionDetail
        if (transaction.getStatus() != TransactionStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_UPDATE_ITEMS_NON_DRAFT);

        TransactionDetail transactionDetail = transaction.getTransactionDetails().stream()
                .filter(details -> details.getId().equals(detailsId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_TRANSACTION_DETAIL_NOT_FOUND));

        Product product = productService.getOne(request.getProductId());
        transactionDetail.setProduct(product);
        transactionDetail.setQty(request.getQty());
        transactionDetail.setPrice(product.getPrice());
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return Mapper.toTransactionResponse(updatedTransaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse checkoutTransaction(String transactionId) {
        Transaction transaction = getOne(transactionId);
        if (transaction.getStatus() != TransactionStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_CHECKOUT_ITEM_FROM_NON_DRAFT);

        transaction.setStatus(TransactionStatus.PENDING);
        for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
            Product product = transactionDetail.getProduct();
            product.setStock(product.getStock() - transactionDetail.getQty());
            productService.updateProduct(product);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return Mapper.toTransactionResponse(updatedTransaction);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse deleteTransactionDetails(String transactionId, String detailsId) {
        Transaction transaction = getOne(transactionId);
        if (transaction.getStatus() != TransactionStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_REMOVE_ITEMS_FROM_NON_DRAFT);

        transaction.getTransactionDetails().stream().findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction detail is empty")
        );
        transaction.getTransactionDetails().removeIf(details -> details.getId().equals(detailsId));
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return Mapper.toTransactionResponse(updatedTransaction);
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getTransactionById(String transactionId) {
        Transaction transaction = getOne(transactionId);
        return Mapper.toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    @Override
    public Transaction getOne(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_TRANSACTION_NOT_FOUND));
        transaction.getTransactionDetails().size();
        return transaction;
    }
}