package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.Store;

import java.util.List;

public interface StoreService {
    Store createStore(Store store);
    Store getStoreById(String id);
    List<Store> getAllStores();
    Store updateStore(String id, Store store);
    void deleteStore(String id);
}
