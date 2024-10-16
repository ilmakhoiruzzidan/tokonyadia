package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.SearchSellerRequest;
import com.enigma.tokonyadia_api.dto.request.SellerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.SellerRequest;
import com.enigma.tokonyadia_api.dto.response.SellerResponse;
import com.enigma.tokonyadia_api.entity.Seller;
import org.springframework.data.domain.Page;

public interface SellerService {
    SellerResponse create(SellerCreateRequest request);

    Seller create(Seller seller);

    SellerResponse getSellerById(String id);

    Page<SellerResponse> getAllSellers(SearchSellerRequest request);

    SellerResponse updateSeller(String id, SellerRequest request);

    void deleteSeller(String id);

    Seller getOne(String id);
}
