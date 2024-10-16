package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.SearchStoreRequest;
import com.enigma.tokonyadia_api.dto.request.StoreRequest;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.StoreRepository;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.specification.StoreSpecification;
import com.enigma.tokonyadia_api.util.SortUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@AllArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    public final StoreRepository storeRepository;

    @Override
    public StoreResponse createStore(StoreRequest request) {
        Store store = Store.builder()
                .name(request.getName())
                .noSiup(request.getNoSiup())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();
        storeRepository.saveAndFlush(store);
        return Mapper.toStoreResponse(store);
    }

    @Override
    public StoreResponse getStoreById(String id) {
        Store store = getOne(id);
        return Mapper.toStoreResponse(store);
    }

    @Override
    public Page<StoreResponse> getAllStores(SearchStoreRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Store> storeSpecification = StoreSpecification.getSpecification(request);
        Page<Store> storePage = storeRepository.findAll(storeSpecification, pageable);
        return storePage.map(Mapper::toStoreResponse);
    }

    @Override
    public StoreResponse updateStore(StoreRequest request, String id) {
        Store newStore = getOne(id);
        newStore.setName(request.getName());
        newStore.setNoSiup(request.getNoSiup());
        newStore.setAddress(request.getAddress());
        newStore.setPhoneNumber(request.getPhoneNumber());
        storeRepository.save(newStore);
        return Mapper.toStoreResponse(newStore);
    }

    @Override
    public void deleteStore(String id) {
        Store store = getOne(id);
        if (store == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_STORE_NOT_FOUND);
        storeRepository.delete(store);
    }


    @Override
    public Store getOne(String id) {
        Optional<Store> store = storeRepository.findById(id);
        return store.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_STORE_NOT_FOUND));
    }


}

