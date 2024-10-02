package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.StoreRepository;
import com.enigma.tokonyadia_api.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {
    public final StoreRepository storeRepository;

    @Override
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Store getStoreById(String id) {
        Optional<Store> byId = storeRepository.findById(id);
        if(byId.isEmpty()){
            throw new RuntimeException("Data store tidak ditemukan");
        }
        return byId.get();
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Store updateStore(String id, Store store) {
        Optional<Store> selectedStore = storeRepository.findById(id);
        if(selectedStore.isPresent()){
            Store newStore = selectedStore.get();
            newStore.setNoSiup(store.getNoSiup());
            newStore.setAddress(store.getAddress());
            newStore.setPhoneNumber(store.getPhoneNumber());
            return storeRepository.save(newStore);
        }
        throw new RuntimeException("Data store tidak ditemukan");
    }

    @Override
    public void deleteStore(String id) {

    }


}
