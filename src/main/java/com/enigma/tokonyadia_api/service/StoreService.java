package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.SearchStoreRequest;
import com.enigma.tokonyadia_api.dto.request.StoreRequest;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StoreService {
    StoreResponse createStore(StoreRequest request);

    StoreResponse getStoreById(String id);

    Page<StoreResponse> getAllStores(SearchStoreRequest request);

    StoreResponse updateStore(StoreRequest request, String id);

    void deleteStore(String id);

    Store getOne(String id);
}
