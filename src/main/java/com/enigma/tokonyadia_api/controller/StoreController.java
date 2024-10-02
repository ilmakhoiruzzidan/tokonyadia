package com.enigma.tokonyadia_api.controller;


import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = Constant.PATH_STORE)
public class StoreController {

    public final StoreService storeService;

    @PostMapping
    public Store saveStore(@RequestBody Store store) {
        return storeService.createStore(store);
    }

    @GetMapping
    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    public Store getStoreById(@PathVariable String id) {
        return storeService.getStoreById(id);
    }

    @PutMapping("/{id}")
    public Store updateStore(@PathVariable String id, @RequestBody Store store) {
        return storeService.updateStore(id, store);
    }

    @DeleteMapping("/{id}")
    public String deleteStore(@PathVariable String id) {
        return storeService.deleteStore(id);
    }
}
