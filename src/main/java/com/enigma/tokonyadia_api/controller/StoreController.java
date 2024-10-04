package com.enigma.tokonyadia_api.controller;


import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.dto.request.StoreRequest;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.utils.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = Constant.PATH_STORE)
public class StoreController {

    @Autowired
    public final StoreService storeService;

    @PostMapping
    public ResponseEntity<?> saveStore(@RequestBody StoreRequest request) {
        StoreResponse storeResponse = storeService.createStore(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_STORE, storeResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllStores(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        PagingAndSortingRequest pagingAndSortingRequest = PagingAndSortingRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();
        Page<StoreResponse> allStores = storeService.getAllStores(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePagination(HttpStatus.OK, Constant.SUCCESS_GET_ALL_STORE, allStores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable String id) {
        StoreResponse storeResponse = storeService.getStoreById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_STORE_BY_ID, storeResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStore(@RequestBody StoreRequest request, @PathVariable String id) {
        StoreResponse storeResponse = storeService.updateStore(request, id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_STORE, storeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable String id) {
        storeService.deleteStore(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_STORE, null);
    }
}
