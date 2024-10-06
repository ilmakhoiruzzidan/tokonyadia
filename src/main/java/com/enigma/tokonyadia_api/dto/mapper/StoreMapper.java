package com.enigma.tokonyadia_api.dto.mapper;

import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Store;


public class StoreMapper {
    public static StoreResponse toStoreResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .noSiup(store.getNoSiup())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress())
                .build();
    }

    public static Store toStore(StoreResponse storeResponse) {
        if (storeResponse == null) {
            throw new IllegalArgumentException("StoreResponse tidak boleh null");
        }
        return Store.builder()
                .id(storeResponse.getId())
                .name(storeResponse.getName())
                .phoneNumber(storeResponse.getPhoneNumber())
                .address(storeResponse.getAddress())
                .noSiup(storeResponse.getNoSiup())
                .build();
    }
}
