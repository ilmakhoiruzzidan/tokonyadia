package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import com.enigma.tokonyadia_api.repository.TransactionDetailRepository;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.TransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final TransactionDetailRepository transactionDetailRepository;
    private final ProductService productService;

    @Override
    public List<TransactionDetail> addTransactionDetail(Transaction transaction, List<TransactionDetailRequest> transactionDetailRequests) {

        ArrayList<TransactionDetail> transactionDetails = new ArrayList<>();

        for (TransactionDetailRequest transactionDetail : transactionDetailRequests) {
            Product product = productService.getOne(transactionDetail.getProductId());

            TransactionDetail td = TransactionDetail.builder()
                    .product(product)
                    .qty(transactionDetail.getQty())
                    .transaction(transaction)
                    .price(product.getPrice())
                    .build();


            transactionDetailRepository.save(td);

            transactionDetails.add(td);
        }

        return transactionDetails;
    }
}
